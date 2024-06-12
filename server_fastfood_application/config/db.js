const mongooes = require('mongoose')
mongooes.set('strictQuery', true)
const url_db = "mongodb+srv://kinaad24:thien2004@fastfood.rhdcjzv.mongodb.net/FastFood"

const connect = async () => {
    try {
        await mongooes.connect(url_db)
        console.log('connect success');
    } catch (error) {
        console.log("error: " + error);
        console.log("connect failed");
    }
}

module.exports = {connect}