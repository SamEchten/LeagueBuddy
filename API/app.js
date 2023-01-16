const express = require("express")
const dotenv = require("dotenv")
const path = require("path")
const https = require("https")
const fs = require("fs")
const { Client, GatewayIntentBits } = require("discord.js")
const admin = require("firebase-admin")

dotenv.config()

const serviceAccount = {
    type: process.env.TYPE,
    project_id: process.env.PROJECT_ID,
    private_key_id: process.env.PRIVATE_KEY_ID,
    private_key: process.env.PRIVATE_KEY,
    client_email: process.env.CLIENT_EMAIL,
    client_id: process.env.CLIENT_ID,
    auth_uri: process.env.AUTH_URI,
    token_uri: process.env.TOKEN_URI,
    auth_provider_x509_cert_url: process.env.AUTH_PROVIDER_CERT_URL,
    client_x509_cert_url: process.env.CLIENT_CERT_URL
};

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
})

const httpsOptions = {
    key: fs.readFileSync("./cert/key.pem"),
    cert: fs.readFileSync("./cert/cert.pem")
}

const app = express()
app.use(express.json())
const server = https.createServer(httpsOptions, app)

const client = new Client({
    intents: [
        GatewayIntentBits.Guilds,
        GatewayIntentBits.GuildMembers,
        GatewayIntentBits.DirectMessages,
        GatewayIntentBits.MessageContent
    ]
})

client.on("ready", () => {
    const discordRouter = require("./routes/discordRouter")
    app.use(discordRouter)

    server.listen(4848, () => {
        console.log("Listening on port 4848")
        console.log("Bot is ready")
    })
})

client.login(process.env.DISCORD_TOKEN)

module.exports = { client, admin }