const crypto = require('crypto');
const fs = require("fs")

const private_key = fs.readFileSync("./cert/private_key.pem")

const decrypt = (cipherText) => {
    const decryption = crypto.privateDecrypt({
        key: private_key,
        padding: crypto.constants.RSA_PKCS1_PADDING
    }, Buffer.from(cipherText, 'base64'));
    return decryption.toString()
}

module.exports = { decrypt }