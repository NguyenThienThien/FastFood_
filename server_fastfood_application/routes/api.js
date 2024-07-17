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
        const user_id = req.query.user_id; // Lấy user_id từ query string

        // Thực hiện truy vấn và in kết quả
        const data = await cart.find({ user_id: user_id });    

        if (data.length > 0) {
            res.status(200).send(data);
        } else {
            res.json({
                "status": 400,
                "messenger": "Get Cart failed",
                "data": []
            });
        }
    } catch (error) {
        console.log(error);
        res.status(500).json({
            "status": 500,
            "messenger": "Server error",
            "data": []
        });
    }
});

router.get('/get-quantity/:id', async (req, res) => {
    try {
        const { id } = req.params;

        const cartItem = await cart.findById(id);

        if (cartItem) {
            res.status(200).json({
                "status": 200,
                "message": "Get quantity success",
                "data": { quantity_cart: cartItem.quantity_cart }
            });
        } else {
            res.status(404).json({
                "status": 404,
                "message": "Cart item not found",
                "data": []
            });
        }
    } catch (error) {
        console.log(error);
        res.status(500).json({
            "status": 500,
            "message": "Server error",
            "data": []
        });
    }
});

router.post('/add-cart', async (req,res) => {
    try {
        const data = req.body
        const existingCart = await cart.findOne({ user_id: data.user_id, product_id: data.product_id})

        if(existingCart){
            existingCart.quantity_cart += data.quantity_cart
            const updatedCart = await existingCart.save()
            res.json({
                "status": 200,
                "messenger": "Update Cart success",
                "data": updatedCart
            })
        }else{
            const newCart = new cart({
                user_id: data.user_id,
                product_id: data.product_id,
                nameProduct: data.nameProduct,
                priceProduct: data.priceProduct,
                imageProduct: data.imageProduct,
                quantity_cart: data.quantity_cart,
                rate: data.rate,
                sold: data.sold
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
        }
    } catch (error) {
        console.log(error);
    }
})

router.delete('/delete-cartById/:id', async (req, res) => {
    try {
        const { id } = req.params

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

router.put('/update-quantity/:id', async (req, res) => {
    try {
        const { id } = req.params;
        const { quantity_cart } = req.body;

        if (!quantity_cart && quantity_cart !== 0) {
            return res.status(400).json({
                "status": 400,
                "message": "quantity_cart is required",
                "data": []
            });
        }

        const cartItem = await cart.findById(id);
        if (!cartItem) {
            return res.status(404).json({
                "status": 404,
                "message": "Cart item not found",
                "data": []
            });
        }

        cartItem.quantity_cart = quantity_cart;
        const updatedCartItem = await cartItem.save();

        res.json({
            "status": 200,
            "message": "Update quantity success",
            "data": updatedCartItem
        });
    } catch (error) {
        console.log(error);
        res.status(500).json({
            "status": 500,
            "message": "Server error",
            "data": []
        });
    }
});


module.exports = router;