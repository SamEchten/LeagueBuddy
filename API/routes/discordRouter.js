const { ChannelType } = require("discord.js")
const express = require("express")
const { client } = require("../app")
const router = express.Router()
const { authUser } = require("../auth/auth")
const { decrypt } = require("../encryption/crypto")

//Creates a discord channel for the users team / game
//Sends 
router.post("/api/discord/createChannel", authUser, async (req, res) => {
    try {
        validateRequest(req)
        let id = decrypt(req.body.user.discordId)
        let gameId = req.body.game.gameId
        let teamCode = req.body.game.teamCode

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
        console.log(e)
        res.statusMessage = e.message
        res.sendStatus(400)
    }
})

const validateRequest = (req) => {
    if (!req.body.user || !req.body.game) {
        throw new Error("Please provide user and game data.")
    }

    if (!req.body.user.discordId ||
        !req.body.user.authKey ||
        !req.body.game.gameId ||
        !req.body.game.teamCode) {
        throw new Error("Please provide the following fields: {discordId, authKey, gameId, teamCode}")
    }
}

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

        if (user.username.toLowerCase() == username &&
            user.discriminator == discriminator) {
            userId = user.id
        }
    })

    return userId
}

module.exports = router