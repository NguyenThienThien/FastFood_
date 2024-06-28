var fs = require('fs')
var path = require('path')

var express = require('express');
var router = express.Router();

var product = require('../models/Product')
var productType = require('../models/ProductType')
var cart = require('../models/Cart')
var upload = require('../config/common/upload')

// ====================Product api=========================
router.get('/get-products', async (req, res) => {
    try {
        const data = await product.find()
        if(data){
            res.status(200).send(data)
        }else{
            res.json({
                "status": 400,
                "messenger": "Get Product failed",
                "data": []
            })
        }
    } catch (error) {
        console.log(error);
    }
})

router.get('/get-productsById/:id', async (req, res) => {
    try {
        const { id } = req.params
        const data = await product.findById(id)
        res.status(200).send(data)
    } catch (error) {
        console.log(error);
    }
})

router.get('/get-productsByTypeId/:productTypeId', async (req, res) => {
    try {
        const{ productTypeId } = req.params
        const data = await product.find({ productTypeId: productTypeId })
        if(data){
            res.status(200).send(data)
        }else{
            res.json({
                "status": 400,
                "messenger": "Get Product failed",
                "data": []
            })
        }
    } catch (error) {
        console.log(error);
    }
})

// ====================Product Type api=========================
router.get('/get-productTypes', async (req, res) => {
    try {
        const data = await productType.find()
        if(data){
            res.status(200).send(data)
        }else{
            res.json({
                "status": 400,
                "messenger": "Get Product Type failed",
                "data": []
            })
        }
    } catch (error) {
        
    }
})

// ====================Cart api=========================
router.get('/get-cartByUserId', async (req, res) => {
    try {
        const { user_id } = req.params
        const data = await cart.find({ user_id: user_id })
        if(data){
            res.status(200).send(data)
        }else{
            res.json({
                "status": 400,
                "messenger": "Get Cart failed",
                "data": []
            })
        }
    } catch (error) {
        console.log(error);
    }
})

router.post('/add-cart', async (req,res) => {
    try {
        const data = req.body

        const newCart = new cart({
            user_id: data.user_id,
            product_id: data.product_id,
            nameProduct: data.nameProduct,
            priceProduct: data.priceProduct,
            imageProduct: data.imageProduct,
            quantity_cart: data.quantity_cart
        })

        const result = await newCart.save()

        if(result){
            res.json({
                "status": 200,
                "messenger": "Add Cart success",
                "data": result
            })
        }else{
            res.json({
                "status": 400,
                "messenger": "Add Cart failed",
                "data": []
            })
        }
    } catch (error) {
        console.log(error);
    }
})

router.delete('/delete-cartById/:id', async (req, res) => {
    try {
        const { id } = req.params

        const cartItem = await cart.findById(id)
        if(!cartItem){
            return res.status(404).json({
                "status": 404,
                "message": "Cart not found",
                "data": []
            })
        }

        const imageUrls = cartItem.imageProduct
        imageUrls.forEach(async (url) => {
            try {
                const filename = url.split('/').pop();
                const filePath = path.join(__dirname, '..', 'public', 'uploads', filename)
                if(fs.existsSync(filePath)){
                    fs.unlinkSync(filePath)
                    console.log(`Deleted file: ${filename}`);
                }else{
                    console.log(`File not found: ${filename}`);
                }
            } catch (error) {
                console.error(`Error deleting file: ${error.message}`);
            }
        })

        const result = await cart.findByIdAndDelete(id)
        if(result){
            res.json({
                "status": 200,
                "messenger": "Delete Cart success",
                "data": result
            })
        }else{
            res.json({
                "status": 400,
                "messenger": "Delete Cart failed",
                "data": []
            })
        }
    } catch (error) {
        console.log(error);
    }
})

router.delete('/delete-all-cartsByUserId/:user_id', async (req, res) => {
    try {
        const { user_id } = req.params;

        const carts = await cart.find({ user_id: user_id })
        if (!carts || carts.length === 0) {
            return res.status(404).json({
                "status": 404,
                "message": "No carts found for this user_id",
                "data": []
            });
        }

        for (let cartItem of carts) {
            const imageUrls = cartItem.imageProduct || []
            imageUrls.forEach(async (url) => {
                try {
                    const filename = url.split('/').pop()
                    const filePath = path.join(__dirname, '..', 'public', 'uploads', filename)
                    if (fs.existsSync(filePath)) {
                        fs.unlinkSync(filePath);
                        console.log(`Deleted file: ${filename}`)
                    } else {
                        console.log(`File not found: ${filename}`)
                    }
                } catch (error) {
                    console.error(`Error deleting file: ${error.message}`)
                }
            });

            await cart.findByIdAndDelete(cartItem._id)
            console.log(`Deleted cart: ${cartItem._id}`)
        }

        res.json({
            "status": 200,
            "message": "Delete all carts success",
            "data": []
        })

    } catch (error) {
        console.log(error);
    }
})

router.put('/update-cartQuantity/:cart-id', async (req,res) => {
    try {
        const { cart_id } = req.params
        const { quantity } = req.body

        const cartItem = await cart.findById(cart_id)
        if(!cartItem){
            return res.status(404).json({
                "status": 404,
                "message": "Cart not found",
                "data": []
            })
        }

        cartItem.quantity_cart = quantity
        await cartItem.save()

        res.json({
            "status": 200,
            "messenger": "Update Cart success",
            "data": cartItem
        })
    } catch (error) {
        console.log(error);
    }
})

module.exports = router;