const { ChannelType } = require("discord.js")
const express = require("express")
const { client } = require("../app")
const router = express.Router()
const { authUser } = require("../auth/auth")

//Send message to discord user
//Body should contain the discordId
//body: {"discordId": "example#1234"}
router.post("/api/discord/createChannel", authUser, async (req, res) => {
    try {
        let id = req.body.user.discordId
        let gameId = req.body.game.gameId
        let teamCode = req.body.game.teamCode

        if (id == null || gameId == null || teamCode == null) {
            res.statusMessage = "Invalid body format"
            res.sendStatus(400).end()
            return
        }

        let user = await fetchUser(id)
        if (user == null) {
            res.statusMessage = "Could not find user with given id."
            res.sendStatus(400).end()
            return
        }

        let channel
        let channelName = gameId + "#" + teamCode
        if (!await channelExists(channelName)) {
            channel = await createChannel(channelName)
        } else {
            channel = await findChannelByName(channelName)
        }

        let inviteLink = await createLink(channel)
        user.send(inviteLink)
        res.sendStatus(200)
    } catch (e) {
        res.statusMessage = "Something went wrong, please try again."
        res.send(400)
    }
})

const findChannelByName = async (channelName) => {
    const g = await guild()
    return await g.channels.cache.find(c => c.name === channelName)
}

const channelExists = async (channelName) => {
    let channel = await findChannelByName(channelName)
    if (channel == null) {
        return false
    }
    return true
}

const createChannel = async (channelName) => {
    const g = await guild()
    let channel = g.channels.create({
        name: channelName,
        type: ChannelType.GuildVoice,
        userLimit: 5
    })
    return channel
}

const createLink = async (channel) => {
    let invite = await channel.createInvite()
    let code = invite.code
    return "https://discord.gg/" + code + ""
}

const fetchUser = async (discordId) => {
    try {
        let id = await getUser(discordId)
        return await client.users.fetch(id)
    } catch (e) {
        return null
    }
}

const guild = async () => {
    return await client.guilds.fetch(process.env.GUILD_ID)
}

const getUser = async (discordId) => {
    const g = await guild()
    const list = await g.members.fetch()

    const username = discordId.split("#")[0]
    const discriminator = discordId.split("#")[1]

    let userId

    list.forEach((value, key) => {
        let user = value["user"]
        if (user.username == username &&
            user.discriminator == discriminator) {
            userId = user.id
        }
    })

    return userId
}

module.exports = router