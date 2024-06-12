const mongoose = require('mongoose')
const Scheme = mongoose.Schema

const productType = new Scheme({
    typeName: {type: String},
    imageProductType: {type: String}
}, {
    timestamps: true
})

module.exports = mongoose.model("ProductType", productType)