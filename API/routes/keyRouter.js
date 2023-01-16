const express = require("express")
const router = express.Router()
const fs = require("fs")

router.get("/api/publickey", async (req, res) => {
    let publicKey = fs.readFileSync("./cert/public_key.pem", "utf-8")
    res.send({ publicKey: publicKey })
})

module.exports = router