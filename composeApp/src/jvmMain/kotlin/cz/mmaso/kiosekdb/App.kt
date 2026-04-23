package cz.mmaso.kiosekdb

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.filled.KeyboardBackspace
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SpaceBar
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.iconSize
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.plus
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cz.mmaso.kiosekdb.scanner.PassportScannerBroker
import cz.mmaso.kiosekdb.theme.MyShapes
import cz.mmaso.kiosekdb.theme.MyTypography
import cz.mmaso.kiosekdb.viewModels.AppViewModel
import kiosekdb.composeapp.generated.resources.HomeInfoTextDenger
import org.jetbrains.compose.resources.painterResource
import kotlinx.serialization.Serializable

import kiosekdb.composeapp.generated.resources.Res
import kiosekdb.composeapp.generated.resources.UserFlyCodePageHelpText1
import kiosekdb.composeapp.generated.resources.UserFlyCodePageLabelNext
import kiosekdb.composeapp.generated.resources.UserIdentificationFormNameLabel
import kiosekdb.composeapp.generated.resources.UserIdentificationFormPassMale_FemaleLabel
import kiosekdb.composeapp.generated.resources.UserIdentificationFormRqBirthDateLabel
import kiosekdb.composeapp.generated.resources.UserIdentificationFormRqCountryPlaceHolder
import kiosekdb.composeapp.generated.resources.UserIdentificationFormRqLastNameLabel
import kiosekdb.composeapp.generated.resources.UserIdentificationFormRqPassDateExpiratedLabel
import kiosekdb.composeapp.generated.resources.UserIdentificationFormRqPassNumberPlaceHolder
import kiosekdb.composeapp.generated.resources.UserIdentificationPageHelpText1
import kiosekdb.composeapp.generated.resources.UserMasterPageLabel01
import kiosekdb.composeapp.generated.resources.UserProtokolPageLabel
import kiosekdb.composeapp.generated.resources.UserReceiptPageLabel
import kiosekdb.composeapp.generated.resources.UserReceiptPageLabelNext
import kiosekdb.composeapp.generated.resources.cestina
import kiosekdb.composeapp.generated.resources.china
import kiosekdb.composeapp.generated.resources.china_flag
import kiosekdb.composeapp.generated.resources.compose_multiplatform
import kiosekdb.composeapp.generated.resources.czech_republic_flag
import kiosekdb.composeapp.generated.resources.david
import kiosekdb.composeapp.generated.resources.english
import kiosekdb.composeapp.generated.resources.euFlag
import kiosekdb.composeapp.generated.resources.israel
import kiosekdb.composeapp.generated.resources.israel_flag
import kiosekdb.composeapp.generated.resources.kod_letu
import kiosekdb.composeapp.generated.resources.korea
import kiosekdb.composeapp.generated.resources.potvrzeni_vystupu
import kiosekdb.composeapp.generated.resources.registrace_uctenek
import kiosekdb.composeapp.generated.resources.registrace_zadatele
import kiosekdb.composeapp.generated.resources.run
import kiosekdb.composeapp.generated.resources.rusky
import kiosekdb.composeapp.generated.resources.russia_flag
import kiosekdb.composeapp.generated.resources.south_korea_flag
import kiosekdb.composeapp.generated.resources.united_kingdom
import kiosekdb.composeapp.generated.resources.united_kingdom_flag
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import kotlin.io.path.Path
import kotlin.io.path.moveTo
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Serializable
object Home

@Serializable
object Settings

val lightBlue = Color(0xff337ab7)
val blue = Color(0xff135694)
val orange = Color(0xffFFD500)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KiosekRoot( model:AppViewModel ) {
    val navController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(-1 ) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Dashboard") },
                    actions = {
                        IconButton(onClick = { /* Handle search */ }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                    },
                    navigationIcon = {
                        if( selectedItem > -1 ) {
                            IconButton(onClick = {
                                navController.popBackStack()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Localized description"
                                )
                            }
                        }
                    },
                )
            },
            content = { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    NavHost(
                        modifier = Modifier.padding(0.dp),
                        navController = navController,
                        startDestination = Home
                    ) {
                        composable<Home> {
                            selectedItem = -1
                            SelectLanguageScreen( model, navController ) { idx ->
                                when( idx ) {
                                    0 -> {
                                        java.util.Locale.setDefault(java.util.Locale.forLanguageTag("cz-CZ"))
                                    }
                                    1 -> {
                                        java.util.Locale.setDefault(java.util.Locale.forLanguageTag("cz-CZ"))
                                    }
                                    2 -> {
                                        java.util.Locale.setDefault(java.util.Locale.forLanguageTag("cz-CZ"))
                                    }
                                    3 -> {
                                        java.util.Locale.setDefault(java.util.Locale.forLanguageTag("cz-CZ"))
                                    }
                                    4 -> {
                                        java.util.Locale.setDefault(java.util.Locale.forLanguageTag("cz-CZ"))
                                    }
                                    5 -> {
                                        java.util.Locale.setDefault(java.util.Locale.forLanguageTag("cz-CZ"))
                                    }
                                }
                            }
                            // HomeScreen( model, navController )
                        }
                        composable<Settings> {
                            selectedItem = 0
                            ScanPassportScreen( model, navController ) { idx ->

                            }
                            // SettingsScreen( model, navController )
                        }
                    }
                }
            }
        )
    }
}

fun getColorByIdx(pageIdx: Int, currentIdx: Int ) : Color {
    if( pageIdx == currentIdx ) {
        return blue
    }
    if( pageIdx < currentIdx ) {
        return lightBlue
    }
    if( pageIdx > currentIdx ) {
        return Color.LightGray
    }
    return Color.LightGray
}

@Composable
fun KiosekRoot2( model:AppViewModel ) {

    val showProgress = model.showProgress.collectAsStateWithLifecycle()
    val scannerIsReady = model.scannerIsReady.collectAsStateWithLifecycle()
    val pas = PassportScannerBroker.passport.collectAsStateWithLifecycle()
    var selectedItem by remember { mutableIntStateOf(0 ) }
    val pagerState = rememberPagerState( pageCount = { 6 } )
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    var Passport by remember { mutableStateOf<Passport?>( null ) }
    var isFilled by remember { mutableStateOf(true) }

    val animatedColor by animateColorAsState(
        targetValue = if (isFilled) Color.Blue else Color.Transparent,
        animationSpec = tween(durationMillis = 1000) // Animace potrvá 1 sekundu
    )

    LaunchedEffect( pas.value ) {
        // doslo ke scanovani pasu
        Passport = PassportScannerBroker.getLast()
        Passport?.let {
            model.setPassport( it )
            pagerState.scrollToPage(2)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background( orange)
    ) {
        Scaffold(
            topBar = {

                val overlapInDp  = 26.dp
                val odsazeniInDp = 32.dp

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(104.dp)
                        .background(orange)
                        .padding( 2.dp ),
                    horizontalArrangement = Arrangement.spacedBy(-overlapInDp)
                ) {
                    if( pagerState.currentPage == 0 ) {
                        TopBarSelectLanguage()
                    }else {
                        TopBar(selectedItem)
                    }
                }
            },
            content = { innerPadding ->
                Box( modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xffFFD500))
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.weight(1f),
                            // 3. Zákaz scrollování gestem (uživatel nemůže prstem přejet)
                            userScrollEnabled = false
                        ) { page ->
                            when (page) {
                                0 -> {
                                    selectedItem = 0
                                    SelectLanguageScreen(model, navController) { idx ->
                                        when( idx ) {
                                            0 -> {
                                                java.util.Locale.setDefault(java.util.Locale.forLanguageTag("cs-CZ"))
                                            }
                                            1 -> {
                                                java.util.Locale.setDefault(java.util.Locale.forLanguageTag("en-US"))
                                            }
                                            2 -> {
                                                java.util.Locale.setDefault(java.util.Locale.forLanguageTag("ru-RU"))
                                            }
                                            3 -> {
                                                java.util.Locale.setDefault(java.util.Locale.forLanguageTag("zh-CN"))
                                            }
                                            4 -> {
                                                java.util.Locale.setDefault(java.util.Locale.forLanguageTag("ko-KR"))
                                            }
                                            5 -> {
                                                java.util.Locale.setDefault(java.util.Locale.forLanguageTag("he-IL"))
                                            }
                                        }
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(1)
                                        }
                                    }
                                }

                                1 -> {
                                    selectedItem = 0
                                    ScanPassportScreen(model, navController) { idx ->

                                    }
                                }

                                2 -> {
                                    selectedItem = 0
                                    KontrolaCloScreen(model, navController)
                                }

                                3 -> {
                                    selectedItem = 1
                                    KodLetuScreen(model, navController)
                                }

                                4 -> {
                                    selectedItem = 2
                                    SeznamUctenekScreen(model, navController)
                                }

                                5 -> {
                                    selectedItem = 3
                                    FinalScreen(model, navController)
                                }

                                else -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Stránka č. ${page + 1}",
                                            style = MaterialTheme.typography.headlineLarge
                                        )
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    scope.launch {
                                        // Animovaný posun na předchozí stránku
                                        if (pagerState.currentPage > 0) {
                                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                        }
                                    }
                                }
                            ) {
                                Text("Předchozí")
                            }

                            Button(
                                onClick = {
                                    scope.launch {
                                        // Animovaný posun na další stránku
                                        if (pagerState.currentPage < pagerState.pageCount - 1) {
                                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                        }
                                    }
                                }
                            ) {
                                Text("Další")
                            }

                            Button(
                                onClick = {
                                    scope.launch {
                                        isFilled = !isFilled
                                    }
                                }
                            ) {
                                Text("Filled")
                            }

                            Button(
                                onClick = {
                                    selectedItem++
                                    selectedItem = selectedItem % 4
                                }
                            ) {
                                Text("Move")
                            }

                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.scrollToPage( 0 )
                                    }
                                }
                            ) {
                                Text("Start")
                            }

                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        playSound("files/beep.wav")
                                    }
                                }
                            ) {
                                Text("Play")
                            }
                        }
                    }
                }
            }
        )
        // dialogy
        Box( modifier = Modifier.fillMaxSize()) {
            Row( modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End ) {
                Box( modifier = Modifier
                    .padding( 16.dp )
                    .size(32.dp)
                    .background( color = if( scannerIsReady.value ) Color.Green else Color.Red, shape = CircleShape )
                ) {

                }
            }
        }

        if( showProgress.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x7f000000)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(80.dp), color = Color.White)
            }
        }
    }
}

@Composable
fun TopBarSelectLanguage() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(blue )
            .padding( 0.dp ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(32.dp))
        Text( text = stringResource(Res.string.david), fontSize = 50.sp, color = orange, fontWeight = FontWeight.Bold )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            modifier = Modifier
                .width( 140.dp)
                .padding( vertical = 8.dp )
                .border( 3.dp, orange, shape = RoundedCornerShape( 6.dp )),
            contentScale = ContentScale.Inside,
            painter = painterResource(Res.drawable.euFlag),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(54.dp))
    }
}

@Composable
fun TopBar( selectedItem: Int ) {
    val overlapInDp  = 26.dp
    val odsazeniInDp = 32.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(104.dp)
            .background(orange)
            .padding( 2.dp ),
        horizontalArrangement = Arrangement.spacedBy(-overlapInDp)
    ) {
        Box( modifier = Modifier.weight(1f).fillMaxHeight().zIndex(4f),
            contentAlignment = Alignment.Center ) {
            val c = getColorByIdx( 0, selectedItem )
            DrawArrow( c, odsazeniInDp )
            Row(
                modifier = Modifier.padding( horizontal = 42.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(Modifier.width(8.dp))
                Icon( modifier = Modifier.size(48.dp), imageVector = Icons.Default.Person, contentDescription = "Person", tint = Color.White )
                Text( modifier = Modifier.weight(1f), text = stringResource(Res.string.UserMasterPageLabel01), textAlign = TextAlign.Center, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .drawBehind {
                            val radiusInPx = size.width / 2f
                            val center = Offset(size.width / 2, size.height / 2)
                            drawCircle(
                                color = Color.White, // Bílá výplň
                                radius = radiusInPx,
                                center = center
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if( selectedItem > 0 )
                    {
                        Icon( modifier = Modifier.size(30.dp), imageVector = Icons.Default.Check, contentDescription = "Person", tint = c )
                    }else {
                        Text("1", color = c, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Box( modifier = Modifier.weight(1f).fillMaxHeight().zIndex(3f),
            contentAlignment = Alignment.Center) {
            // val c = if( selectedItem == 1 ) Color(0xff135694) else Color.LightGray
            val c = getColorByIdx( 1, selectedItem )
            DrawArrow( c, odsazeniInDp )
            Row(
                modifier = Modifier.padding( horizontal = 42.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(Modifier.width(8.dp))
                Icon( modifier = Modifier.size(48.dp), imageVector = Icons.Default.Flight, contentDescription = "Person", tint = Color.White )
                Text( modifier = Modifier.weight(1f), text = stringResource(Res.string.UserFlyCodePageLabelNext), textAlign = TextAlign.Center, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .drawBehind {
                            val radiusInPx = size.width / 2f
                            val center = Offset(size.width / 2, size.height / 2)
                            drawCircle(
                                color = Color.White, // Bílá výplň
                                radius = radiusInPx,
                                center = center
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if( selectedItem > 1 )
                    {
                        Icon( modifier = Modifier.size(30.dp), imageVector = Icons.Default.Check, contentDescription = "Person", tint = c )
                    }else {
                        Text( "2", color = c, fontSize = 20.sp, fontWeight = FontWeight.Bold )
                    }
                }
            }
        }
        Box( modifier = Modifier.weight(1f).fillMaxHeight().zIndex(2f),
            contentAlignment = Alignment.Center) {
            // val c = if( selectedItem == 2 ) Color(0xff135694) else Color.LightGray
            val c = getColorByIdx( 2, selectedItem )
            DrawArrow( c, odsazeniInDp )
            Row(
                modifier = Modifier.padding( horizontal = 42.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(Modifier.width(8.dp))
                Icon( modifier = Modifier.size(48.dp), imageVector = Icons.Default.ShoppingCart, contentDescription = "Person", tint = Color.White )
                Text( modifier = Modifier.weight(1f), text = stringResource(Res.string.UserReceiptPageLabel), textAlign = TextAlign.Center, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .drawBehind {
                            val radiusInPx = size.width / 2f
                            val center = Offset(size.width / 2, size.height / 2)
                            drawCircle(
                                color = Color.White, // Bílá výplň
                                radius = radiusInPx,
                                center = center
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if( selectedItem > 2 )
                    {
                        Icon( modifier = Modifier.size(30.dp), imageVector = Icons.Default.Check, contentDescription = "Person", tint = c )
                    }else {
                        Text("3", color = c, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Box( modifier = Modifier.weight(1f).fillMaxHeight().zIndex(1f),
            contentAlignment = Alignment.Center) {
            // val c = if( selectedItem == 3 ) Color(0xff135694) else Color.LightGray
            val c = getColorByIdx( 3, selectedItem )
            DrawArrow( c, odsazeniInDp )
            Row(
                modifier = Modifier.padding( horizontal = 42.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(Modifier.width(8.dp))
                Icon( modifier = Modifier.size(48.dp), imageVector = Icons.Default.Print, contentDescription = "Person", tint = Color.White )
                Text( modifier = Modifier.weight(1f), text = stringResource(Res.string.UserProtokolPageLabel), textAlign = TextAlign.Center, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .drawBehind {
                            val radiusInPx = size.width / 2f
                            val center = Offset(size.width / 2, size.height / 2)
                            drawCircle(
                                color = Color.White, // Bílá výplň
                                radius = radiusInPx,
                                center = center
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if( selectedItem > 3 )
                    {
                        Icon( modifier = Modifier.size(30.dp), imageVector = Icons.Default.Check, contentDescription = "Person", tint = c )
                    }else {
                        Text("4", color = c, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun DrawArrow( color: Color, odsazeni:Dp = 40.dp ) {
    val density  = LocalDensity.current
    val odsazeni = with(density) { odsazeni.toPx() }

    Canvas( modifier = Modifier.fillMaxSize()) {
        val path = Path().apply {
            moveTo( 0f, 0f )
            lineTo(size.width - odsazeni, 0f )
            lineTo(size.width, size.height / 2f )
            lineTo(size.width - odsazeni, size.height )
            lineTo(0f, size.height )
            lineTo(0f  + odsazeni, size.height / 2f )
            close()
        }
        drawPath(
            path = path,
            color = color
        )
    }
}

@Composable
fun SelectLanguageScreen( model:AppViewModel, navController: NavHostController, onCLick:( idx:Int) -> Unit  ) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {

                Row(
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Column(
                        modifier = Modifier.clickable {
                            onCLick(0)
                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            modifier = Modifier.width(160.dp).background(Color.Yellow),
                            contentScale = ContentScale.Inside,
                            painter = painterResource(Res.drawable.czech_republic_flag),
                            contentDescription = null
                        )
                        Text(stringResource(Res.string.cestina), fontSize = 20.sp)
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(
                        modifier = Modifier.clickable {
                            onCLick(1)
                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            modifier = Modifier.width(160.dp).background(Color.Yellow),
                            contentScale = ContentScale.Inside,
                            painter = painterResource(Res.drawable.united_kingdom),
                            contentDescription = null
                        )
                        Text(stringResource(Res.string.english), fontSize = 20.sp)
                    }

                    Spacer(Modifier.width(700.dp))

                    Column(
                        modifier = Modifier.clickable {
                            onCLick(2)
                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            modifier = Modifier.width(160.dp).background(Color.Yellow),
                            contentScale = ContentScale.Inside,
                            painter = painterResource(Res.drawable.russia_flag),
                            contentDescription = null
                        )
                        Text(stringResource(Res.string.rusky), fontSize = 20.sp)
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(
                        modifier = Modifier.clickable {
                            onCLick(3)
                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            modifier = Modifier.width(160.dp),
                            contentScale = ContentScale.Inside,
                            painter = painterResource(Res.drawable.china_flag),
                            contentDescription = null
                        )
                        Text(stringResource(Res.string.china), fontSize = 20.sp)
                    }

                    Spacer(Modifier.width(350.dp))

                    Column(
                        modifier = Modifier.clickable {
                            onCLick(4)
                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            modifier = Modifier.width(160.dp),
                            contentScale = ContentScale.Inside,
                            painter = painterResource(Res.drawable.south_korea_flag),
                            contentDescription = null
                        )
                        Text(stringResource(Res.string.korea), fontSize = 20.sp)
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Column(
                        modifier = Modifier.clickable {
                            onCLick(5)
                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            modifier = Modifier.width(160.dp),
                            contentScale = ContentScale.Inside,
                            painter = painterResource(Res.drawable.israel_flag),
                            contentDescription = null
                        )
                        Text(stringResource(Res.string.israel), fontSize = 20.sp)
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().background( blue ),
                horizontalArrangement = Arrangement.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("© 2026 - Generální ředitelství cel, Budějovická 7, 140 00 Praha 4", fontSize = 16.sp, color = orange)
                    Text("Ver.: 1.0.1", fontSize = 16.sp, color = orange)
                }
            }
        }
    }
}

@Composable
fun ScanPassportScreen( model:AppViewModel, navController: NavHostController, onCLick:( idx:Int) -> Unit ) {

    DisposableEffect( Unit ) {
        model.startScanner()
        onDispose {
            model.stopScanner()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon( modifier = Modifier.size(128.dp), imageVector = Icons.Default.Person, contentDescription = null, tint = blue )
            Spacer(modifier = Modifier.size(64.dp))
            Text(
                text = stringResource(Res.string.UserIdentificationPageHelpText1),
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = blue
            )
        }
    }
}

@Composable
fun KontrolaCloScreen( model:AppViewModel, navController: NavHostController ) {

    val passport by model.passport.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Column(modifier = Modifier
                .width(200.dp)
                .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier.size(160.dp).padding( end = 0.dp ),
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = blue )
            }
            Column( modifier = Modifier.fillMaxWidth( 0.6f )) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = stringResource(Res.string.HomeInfoTextDenger),
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        color = blue
                    )
                }
                Spacer(modifier = Modifier.size(8.dp))
                Row {
                    Column( modifier = Modifier.weight(0.5f)) {
                        Text( stringResource(Res.string.UserIdentificationFormNameLabel), fontWeight = FontWeight.Bold, fontSize = 18.sp )
                        TextFiled( passport?.transactionData?.jsonGivenName )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text( stringResource(Res.string.UserIdentificationFormRqCountryPlaceHolder))
                        TextFiled(passport?.transactionData?.jsonNationalityCode ?: "" )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(stringResource(Res.string.UserIdentificationFormRqPassDateExpiratedLabel))
                        TextFiled(passport?.transactionData?.jsonExpirationDate ?: "")
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Column( modifier = Modifier.weight(0.5f)) {
                        Text(stringResource(Res.string.UserIdentificationFormRqLastNameLabel))
                        TextFiled(passport?.transactionData?.jsonSurname ?: "" )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(stringResource(Res.string.UserIdentificationFormRqPassNumberPlaceHolder))
                        TextFiled(passport?.transactionData?.jsonDocumentNumber ?: "" )
                        Spacer(modifier = Modifier.size(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(
                                modifier = Modifier.weight(0.5f)
                            ) {
                                Text(stringResource(Res.string.UserIdentificationFormRqBirthDateLabel))
                                TextFiled(passport?.transactionData?.jsonBirthDate ?: "" )
                            }
                            Spacer(modifier = Modifier.size(8.dp))
                            Column(
                                modifier = Modifier.weight(0.5f)
                            ) {
                                Text(stringResource(Res.string.UserIdentificationFormPassMale_FemaleLabel))
                                TextFiled( passport?.transactionData?.jsonSex ?: "" )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TextFiled( str: String? ) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth().border( 2.dp, Color.Gray, RoundedCornerShape( 4.dp ) ),
        value = str ?: "",
        onValueChange = {},
        readOnly = true,
        textStyle = TextStyle( fontSize = 20.sp, fontWeight = FontWeight.Bold ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White
        )
    )
}

@Composable
fun TextFiledEditeable( str: TextFieldValue, onChange:( str: TextFieldValue ) -> Unit, focusReq:FocusRequester ) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .border( 2.dp, Color.Gray, RoundedCornerShape( 4.dp ))
            .focusRequester(focusReq),
        value = str,
        onValueChange = { onChange(it ) },
        readOnly = false,
        textStyle = TextStyle( fontSize = 22.sp, fontWeight = FontWeight.Bold ),
        maxLines = 1,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor   = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor  = Color.White
        )
    )
}

@Composable
fun KodLetuScreen( model:AppViewModel, navController: NavHostController ) {

    val kodletu = model.kodLetu.collectAsStateWithLifecycle()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth( 1f )
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.size(64.dp))
                    Row(
                        modifier = Modifier
                            .offset( x = -64.dp)
                    ) {
                        Column {
                            Icon(
                                modifier = Modifier.size(128.dp),
                                imageVector = Icons.Default.Flight,
                                contentDescription = null,
                                tint = blue
                            )
                        }
                        Spacer(modifier = Modifier.size(32.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth(0.3f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(Res.string.UserFlyCodePageHelpText1),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = blue
                            )
                            TextFiledEditeable(
                                str = kodletu.value,
                                onChange = { str ->
                                    model.setKodeLetu(str.text)
                                },
                                focusRequester
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(32.dp))
                    VirtualKB(
                        onChar = {
                            model.processKb(it)
                        },
                        onKbCtrl = {
                            model.processKbCtrl(it)
                        })
                }
                Row( modifier = Modifier.fillMaxWidth()) {
                    Spacer( Modifier.size(16.dp))
                    Button(
                        modifier = Modifier
                            .weight(0.5f)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor   = Color.Black,
                        ),
                        onClick = {

                        },
                        shape = RoundedCornerShape(6.dp )
                    ) {
                        Icon( imageVector = Icons.Default.Home, modifier = Modifier.size(38.dp), contentDescription = null, tint = blue )
                    }
                    Spacer( Modifier.size(8.dp))
                    Button(
                        modifier = Modifier
                            .weight(0.5f)
                            .height(56.dp),
                        onClick = {

                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = blue,
                            contentColor   = Color.White,
                        ),
                        shape = RoundedCornerShape(6.dp )
                    ) {
                        Icon( imageVector = Icons.Default.Keyboard, modifier = Modifier.size(38.dp), contentDescription = null, tint = Color.White )
                        Spacer( Modifier.size(8.dp))
                        Text( stringResource(Res.string.UserReceiptPageLabelNext), fontSize = 20.sp)
                    }
                    Spacer( Modifier.size(16.dp))
                }
            }
        }

    }
}

@Composable
fun SeznamUctenekScreen( model:AppViewModel, navController: NavHostController ) {
    val items = remember { mutableStateOf<List<String>>( listOf()) }
    val listState = rememberLazyListState()

    LaunchedEffect( Unit ) {
        items.value = mutableListOf( "111", "222", "333", "444", "555", "666", "777", "888", "999" )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.size(128.dp),
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = blue
                )
                Spacer(modifier = Modifier.size(64.dp))
                Text(
                    text = stringResource(Res.string.UserReceiptPageLabel),
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    color = blue
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.7f)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().weight(1f),
                    state = listState,
                ) {
                    items(items = items.value) {
                        ExpandableItem( it )
                    }
                }
                VerticalScrollbar(
                    modifier = Modifier
                        .fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(
                        scrollState = listState
                    )
                )
            }
        }
    }
}

@Composable
fun ExpandableItem( v: String ) {
    var expanded by remember { mutableStateOf(false ) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .clickable {
                expanded = !expanded
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors( containerColor = Color.White )
    ) {
        Column( modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.Red)
                        .width(16.dp)
                        .height(32.dp)
                ) {
                }

                Row(
                    modifier = Modifier.weight(1f).padding( 8.dp ),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.size(16.dp))

                    Column(Modifier.weight(1.3f)) {
                        Text("Název:", fontStyle = FontStyle.Italic, fontSize = 16.sp)
                        Text("Company 1", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }

                    Column(Modifier.weight(0.5f)) {
                        Text("VAT/DIČ:", fontStyle = FontStyle.Italic, fontSize = 16.sp)
                        Text("CZ12345678", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }

                    Column(Modifier.weight(0.5f)) {
                        Text("Č.účtenky:", fontStyle = FontStyle.Italic, fontSize = 16.sp)
                        Text("105896587", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }

                    Column(Modifier.weight(0.5f)) {
                        Text("∑ účtenky")
                        Text("1 503,00")
                    }

                    Column(Modifier.weight(0.5f)) {
                        Text("∑ položek")
                        Text("4")
                    }

                    Column(Modifier.weight(0.5f)) {
                        Text("Datum nákupu")
                        Text("14.04.2026")
                    }

                    Column(
                        Modifier.weight(0.2f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Stav")
                        Icon(
                            modifier = Modifier.size(38.dp),
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.Green
                        )
                    }
                }
            }
            if( expanded )
            {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tady je detailní popis položky, který se objeví až po kliknutí. " +
                                "Díky modifikátoru animateContentSize je rozbalení plynulé.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun FinalScreen( model:AppViewModel, navController: NavHostController ) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon( modifier = Modifier.size(128.dp), imageVector = Icons.Default.Person, contentDescription = null, tint = blue )
            Spacer(modifier = Modifier.size(64.dp))
            Text(
                text = "Naskenujte osobní údaje z pasu",
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = blue
            )
        }
    }
}

@Composable
fun VirtualKB( onChar:( str: String ) -> Unit, onKbCtrl:( kb:VIRTUAL_KEYS ) -> Unit ) {
    Column( modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            KbButton("1") {
                onChar( it )
            }
            KbButton("2") {
                onChar( it )
            }
            KbButton("3") {
                onChar( it )
            }
            KbButton("4") {
                onChar( it )
            }
            KbButton("5") {
                onChar( it )
            }
            KbButton("6") {
                onChar( it )
            }
            KbButton("7") {
                onChar( it )
            }
            KbButton("8") {
                onChar( it )
            }
            KbButton("9") {
                onChar( it )
            }
            KbButton("0") {
                onChar( it )
            }
            KbButtonCtrl( VIRTUAL_KEYS.KB_BACKSPACE ) {
                onKbCtrl(VIRTUAL_KEYS.KB_BACKSPACE)
            }
        }
        Row(
            modifier = Modifier.padding( top = 8.dp )
        ) {
            KbButton("Q") {
                onChar( it )
            }
            KbButton("W") {
                onChar( it )
            }
            KbButton("E") {
                onChar( it )
            }
            KbButton("R") {
                onChar( it )
            }
            KbButton("T") {
                onChar( it )
            }
            KbButton("Y") {
                onChar( it )
            }
            KbButton("U") {
                onChar( it )
            }
            KbButton("I") {
                onChar( it )
            }
            KbButton("O") {
                onChar( it )
            }
            KbButton("P") {
                onChar( it )
            }
        }
        Row(
            modifier = Modifier.padding( top = 8.dp )
        ){
            KbButton("A") {
                onChar( it )
            }
            KbButton("S") {
                onChar( it )
            }
            KbButton("D") {
                onChar( it )
            }
            KbButton("F") {
                onChar( it )
            }
            KbButton("G") {
                onChar( it )
            }
            KbButton("H") {
                onChar( it )
            }
            KbButton("J") {
                onChar( it )
            }
            KbButton("K") {
                onChar( it )
            }
            KbButton("L") {
                onChar( it )
            }
        }
        Row(
            modifier = Modifier.padding( top = 8.dp )
        ) {
            KbButton("Z") {
                onChar(it)
            }
            KbButton("X") {
                onChar(it)
            }
            KbButton("C") {
                onChar(it)
            }
            KbButton("V") {
                onChar(it)
            }
            KbButton("B") {
                onChar(it)
            }
            KbButton("N") {
                onChar(it)
            }
            KbButton("M") {
                onChar(it)
            }
        }
        Row(
            modifier = Modifier.padding( top = 8.dp )
        ) {
            KbButtonCtrl( VIRTUAL_KEYS.KB_SPACE ) {
                onKbCtrl(VIRTUAL_KEYS.KB_SPACE)
            }
        }
    }
}

public enum class VIRTUAL_KEYS {
    KB_BACKSPACE, KB_ENTER, KB_SPACE
}

@Composable
fun KbButtonCtrl( typ: VIRTUAL_KEYS, onClick:( s: VIRTUAL_KEYS ) -> Unit ) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.90f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "ButtonScale"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed) Color.LightGray else Color.White,
        animationSpec = tween(durationMillis = 150), // Barva se mění plynule
        label = "ColorAnimation"
    )

    when (typ) {
        VIRTUAL_KEYS.KB_BACKSPACE -> {
            Box(
                modifier = Modifier
                    .graphicsLayer(scaleX = scale, scaleY = scale)
                    .padding( end = 8.dp )
                    .height(68.dp).width(96.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .border(2.dp, Color.Gray, RoundedCornerShape(6.dp))
                    .background( backgroundColor )
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                isPressed = true
                                tryAwaitRelease()
                                isPressed = false
                            },
                            onTap = {
                                onClick(typ )
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    modifier = Modifier.size(24.dp),
                    contentDescription = "Search")
            }
        }
        VIRTUAL_KEYS.KB_ENTER -> {

        }
        VIRTUAL_KEYS.KB_SPACE -> {
            Box(
                modifier = Modifier
                    .graphicsLayer(scaleX = scale, scaleY = scale)
                    .padding( end = 8.dp )
                    .height(68.dp).width(260.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .border(2.dp, Color.Gray, RoundedCornerShape(6.dp))
                    .background( backgroundColor )
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                isPressed = true
                                tryAwaitRelease()
                                isPressed = false
                            },
                            onTap = {
                                onClick(typ )
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Row {
                    Text( "SPACE", fontSize = 22.sp, fontWeight = FontWeight.Bold )
                    Spacer(Modifier.width(16.dp))
                    Icon(
                        imageVector = Icons.Default.SpaceBar,
                        modifier = Modifier.size(24.dp),
                        contentDescription = "Space"
                    )
                }
            }
        }
    }
}

@Composable
fun KbButton( str: String, onClick:( s: String ) -> Unit ) {

    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.90f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "ButtonScale"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed) Color.LightGray else Color.White,
        animationSpec = tween(durationMillis = 150), // Barva se mění plynule
        label = "ColorAnimation"
    )

    Box(
        modifier = Modifier
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .padding( end = 8.dp )
            .size(68.dp)
            .clip(RoundedCornerShape(6.dp))
            .border(2.dp, Color.Gray, RoundedCornerShape(6.dp))
            .background( backgroundColor )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = {
                        onClick(str )
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text( str, fontSize = 22.sp, fontWeight = FontWeight.Bold )
    }
}

@Composable
fun CircularLayoutExample() {
    val itemCount = 6
    val radius = 400.dp // Poloměr kruhu (vzdálenost od středu)

    // Hlavní obal, který vycentruje vše uvnitř
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        for (i in 0 until itemCount) {
            // Výpočet úhlu pro aktuální prvek.
            // Odečítáme (PI / 2), aby první prvek začínal nahoře (na 12 hodinách).
            val angleInRadians = (i * (2 * PI / itemCount)) - (PI / 2)

            // Výpočet X a Y posunu
            val offsetX = (radius.value * cos(angleInRadians)).dp
            val offsetY = (radius.value * sin(angleInRadians)).dp

            // Samotný obdélníkový objekt
            Box(
                modifier = Modifier
                    .offset(x = offsetX, y = offsetY) // Aplikace vypočítané pozice
                    .size(width = 180.dp, height = 100.dp) // Obdélníkový tvar
                    .background(Color.Blue),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${i + 1}",
                    color = Color.White
                )
            }
        }
    }
}

@Composable
@Preview
fun App() {
    val config = remember { ConfigManager.loadConfig() }
    val httpClient = remember { getHttpClient() }
    val model = viewModel { AppViewModel( config, httpClient ) }
    var languageCode by remember { mutableStateOf("cs") }

    MaterialTheme(
        typography = MyTypography(),
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            KiosekRoot2(model)
            // CircularLayoutExample()
         }
    }
}

// https://127.0.0.1:8443/DocumentScan_axd
//
