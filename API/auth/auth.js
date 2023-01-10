const { getAuth } = require("firebase-admin/auth")

const authUser = async (req, res, next) => {
    if (await validToken(req.body.user.authKey)) {
        next()
    } else {
        res.statusMessage = "User is not authenticated"
        res.sendStatus(400).end()
    }
}

const validToken = async (token) => {
    try {
        await getAuth().verifyIdToken(token)
        return true
    } catch (e) {
        return false
    }
}

module.exports = { authUser }