const mongoose = require('mongoose')
const Scheme = mongoose.Schema

const cart = new Scheme({
    user_id: {type: String},
    product_id: {type: String},
    nameProduct: {type: String},
    priceProduct: {type: Number},
    imageProduct: {type: [String]},
    quantity_cart: {type: Number}
}, {
    timestamps: true
})

module.exports = mongoose.model("Cart", cart)