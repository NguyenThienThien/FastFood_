var express = require('express');
var router = express.Router();

var product = require('../models/Product')
var productType = require('../models/ProductType')

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

module.exports = router;