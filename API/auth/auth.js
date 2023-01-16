const { getAuth } = require("firebase-admin/auth")

const authUser = async (req, res, next) => {
    getAuth().verifyIdToken(req.body.user.authKey)
        .then((decodedToken) => {
            if (decodedToken) {
                next()
            } else {
                res.statusMessage = "User is not authenticated"
                res.sendStatus(400).end()
            }
        })
        .catch((e) => {
            res.statusMessage = "User is not authenticated"
            res.sendStatus(400).end()
        })
}

module.exports = { authUser }