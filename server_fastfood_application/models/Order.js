const mongoose = require('mongoose')
const Scheme = mongoose.Schema

const OrderItem = new Scheme({
    nameProduct: { type: String },
    imageProduct: { type: String },
    priceProduct: { type: Number },
    quantity: { type: Number }
})

const Order = new Scheme({
    userId: { type: String },
    orderItems: [OrderItem],
    totalAmount: { type: Number },
    statusOrder: { type: Number },
    dateOrder: { type: String },
    timeOrder: { type: String }
},{
    timestamps: true
})

module.exports = mongoose.model("order", Order)