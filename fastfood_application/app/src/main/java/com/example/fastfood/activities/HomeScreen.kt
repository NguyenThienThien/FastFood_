package com.example.fastfood.activities

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.fastfood.R
import com.example.fastfood.data.models.Product
import com.example.fastfood.data.models.ProductType
import com.example.fastfood.data.models.productResponseToProduct
import com.example.fastfood.data.models.productTypeResponseToProductType
import com.example.fastfood.ui.theme.OpenSans
import com.example.fastfood.utils.formatCurrency
import com.example.fastfood.viewModel.ProductTypesViewModel
import com.example.fastfood.viewModel.ProductViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    productTypeViewModel: ProductTypesViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel()
) {
    val textFindState = remember { mutableStateOf("") }
    val images = listOf(
        R.drawable.banner1,
        R.drawable.banner2,
        R.drawable.banner3
    )
    val listState = rememberLazyListState()
    var currentItem by remember { mutableStateOf(0) }
    var selectedProductTypeId by remember { mutableStateOf<String?>(null) }

    // Handle data
    val productTypesState by productTypeViewModel.productTypes.observeAsState(emptyList())
    val listProductTypes = productTypesState
    val productState by productViewModel.products.observeAsState(emptyList())
    val listProducts = productState

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            currentItem = (currentItem + 1) % images.size
            listState.animateScrollToItem(currentItem)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(10.dp)
    ) {
        Column {
            Text(
                text = "Find Your Favorite Food",
                fontFamily = OpenSans,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .width(200.dp)
                    .height(80.dp)
            )

            Spacer(modifier = Modifier.padding(10.dp))

            SearchBar(textFindState)

            Spacer(modifier = Modifier.padding(5.dp))

            BannerCarousel(images, listState, currentItem)

            ListProductType(
                productType = listProductTypes.map { productTypeResponseToProductType(it) },
                onProductTypeSelected = { selectedProductTypeId = it }
            )

            ListProducts(
                product = listProducts.map { productResponseToProduct(it) },
                selectedProductTypeId = selectedProductTypeId
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(textFindState: MutableState<String>) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(40.dp))
                .background(Color(0xFFededed))
                .height(55.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(start = 10.dp))
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color(0xFF646464),
                modifier = Modifier.size(30.dp)
            )

            TextField(
                value = textFindState.value,
                onValueChange = { textFindState.value = it },
                placeholder = {
                    Text(
                        text = "Search for food ...",
                        fontFamily = OpenSans,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF646464)
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFededed),
                    focusedIndicatorColor = Color(0xFFededed),
                    unfocusedIndicatorColor = Color(0xFFededed)
                ),
                modifier = Modifier
                    .height(50.dp)
                    .weight(1f)
            )

            Image(
                painter = painterResource(id = R.drawable.filter),
                contentDescription = null,
                modifier = Modifier
                    .size(45.dp)
                    .padding(end = 20.dp)
            )
        }

        Spacer(modifier = Modifier.padding(10.dp))

        Image(
            painter = painterResource(id = R.drawable.notification),
            contentDescription = null,
            modifier = Modifier.size(35.dp)
        )
    }
}

@Composable
fun BannerCarousel(images: List<Int>, listState: LazyListState, currentItem: Int) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(10.dp))
    ) {
        items(images.size) { index ->
            var scale by remember { mutableStateOf(0.95f) }
            val alpha by animateFloatAsState(targetValue = if (currentItem == index) 1f else 0f)

            LaunchedEffect(currentItem) {
                scale = if (currentItem == index) 1f else 0.95f
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        alpha = alpha
                    )
                    .pointerInput(Unit) {
                        detectTransformGestures { _, _, zoom, _ ->
                            scale = (scale * zoom).coerceIn(1f, 3f)
                        }
                    }
            ) {
                Image(
                    painter = painterResource(id = images[index]),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun ListProducts(product: List<Product>, selectedProductTypeId: String?) {
    val isExpanded = remember { mutableStateOf(false) }

    Spacer(modifier = Modifier.padding(5.dp))
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(product.filter { it.productTypeId == selectedProductTypeId }) { products ->
                ItemProduct(product = products)
            }
        }

        Spacer(modifier = Modifier.padding(5.dp))

        Text(
            text = "Popular Menu",
            fontFamily = OpenSans,
            fontSize = 16.sp,
            fontWeight = FontWeight(600),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(5.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            val productsToDisplay = if (isExpanded.value) product else product.distinctBy { it.productTypeId }
            items(productsToDisplay) { product ->
                ItemProduct2(product = product)
            }

            if (!isExpanded.value) {
                item {
                    TextButton(
                        onClick = { isExpanded.value = true },
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Xem thêm",
                            fontFamily = OpenSans,
                            fontSize = 14.sp,
                            fontWeight = FontWeight(500),
                            color = Color(0xFFEC2578)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ItemProduct(product: Product) {
    Box(
        modifier = Modifier
            .width(180.dp)
            .height(155.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .border(width = 1.dp, Color(0xFFe7f0f7))
            .shadow(elevation = 1.dp)
    ) {
        Column {
            // Product image
            AsyncImage(
                model = product.imageProduct[0],
                contentDescription = null,
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.padding(3.dp))

            // Product name
            Text(
                text = product.nameProduct,
                fontFamily = OpenSans,
                fontSize = 16.sp,
                fontWeight = FontWeight(500),
                modifier = Modifier.padding(start = 10.dp),
                textAlign = TextAlign.Center,
            )

            // Product price
            Text(
                text = formatCurrency(product.priceProduct),
                fontFamily = OpenSans,
                fontSize = 14.sp,
                fontWeight = FontWeight(600),
                modifier = Modifier.padding(start = 10.dp),
                textAlign = TextAlign.Center,
                color = Color(0xFFEC2578)
            )
        }

        // Add to cart button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(11.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                modifier = Modifier
                    .height(28.dp)
                    .width(28.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFEC2578)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { /*TODO: Add to cart action*/ }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.padding(5.dp))
}

@Composable
fun ItemProduct2(product: Product) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .border(width = 1.dp, Color(0xFFe7f0f7))
            .shadow(elevation = 1.dp)
            .padding(10.dp),
    ) {
        // Product image
        AsyncImage(
            model = product.imageProduct[0],
            contentDescription = null,
            modifier = Modifier
                .height(70.dp)
                .width(90.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )

        // Product name
        Text(
            text = product.nameProduct,
            fontFamily = OpenSans,
            fontSize = 18.sp,
            fontWeight = FontWeight(500),
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.CenterVertically),
            textAlign = TextAlign.Center,
        )

        // Product price and Add to cart button
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            // Product price
            Text(
                text = formatCurrency(product.priceProduct),
                fontFamily = OpenSans,
                fontSize = 20.sp,
                fontWeight = FontWeight(600),
                modifier = Modifier.padding(start = 10.dp),
                textAlign = TextAlign.Center,
                color = Color(0xFFEC2578)
            )

            // Add to cart button
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color(0xFFF05B99))
                    .height(21.dp)
                    .width(70.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { /*TODO: Add to cart action*/ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Add to cart",
                        fontFamily = OpenSans,
                        fontSize = 10.sp,
                        fontWeight = FontWeight(500),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.padding(5.dp))
}

@Composable
fun ListProductType(productType: List<ProductType>, onProductTypeSelected: (String) -> Unit) {
    Spacer(modifier = Modifier.padding(5.dp))

    // Lưu trữ itemId được chọn
    val selectedItemId = remember { mutableStateOf<String?>(null) }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        items(productType) { types ->
            ItemTypes(
                productType = types,
                isSelected = types.id == selectedItemId.value,
                onItemSelected = { itemId ->
                    selectedItemId.value = itemId
                    onProductTypeSelected(itemId)
                }
            )
        }
    }
}

@Composable
fun ItemTypes(
    productType: ProductType,
    isSelected: Boolean,
    onItemSelected: (String) -> Unit
) {

    val backgroundColor = if (isSelected) Color(0xFFF14E89) else Color.Transparent
    val contentColor = if (isSelected) Color.White else Color.Black

    Row(
        modifier = Modifier
            .width(150.dp)
            .height(43.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFFF14E89), RoundedCornerShape(8.dp))
            .padding(10.dp)
            .clickable {
                // Gọi hàm callback khi ItemTypes được click
                onItemSelected(productType.id)
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = productType.imageProductType,
            contentDescription = null,
            modifier = Modifier
                .width(24.dp)
                .height(24.dp)
                .clip(RoundedCornerShape(50.dp)),
            contentScale = ContentScale.Crop
        )

        Text(
            text = productType.typeName,
            fontFamily = OpenSans,
            fontSize = 16.sp,
            fontWeight = FontWeight(500),
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.Center,
            color = contentColor
        )
    }

    Spacer(modifier = Modifier.padding(5.dp))
}


