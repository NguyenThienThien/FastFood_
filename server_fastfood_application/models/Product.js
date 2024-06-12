const mongoose = require('mongoose')
const Scheme = mongoose.Schema

const product = new Scheme({
    nameProduct: {type: String},
    productTypeId: {type: String},
    priceProduct: {type: Number},
    imageProduct: {type: [String]},
    describeProduct: {type: String},
    statusProduct: {type: Number},
}, {
    timestamps: true
})

module.exports = mongoose.model("Product", product)