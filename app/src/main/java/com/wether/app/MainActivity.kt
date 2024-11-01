package com.wether.app

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.wether.app.common.Constants
import com.wether.app.common.Constants.KEY_CITY
import com.wether.app.common.Constants.SHARED_PREFERENCE_KEY
import com.wether.app.model.WeatherResponseState
import com.wether.app.model.WeatherViewModel
import com.wether.app.ui.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel : WeatherViewModel by viewModels()

    //Create shared pref obj
    var sharedPreference: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialize shared pref
        sharedPreference = getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE)

        setContent {
            WeatherAppTheme {
                //val viewModel = hiltViewModel<WeatherViewModel>()
                if (getCity().isEmpty().not()) {
                    viewModel.city.value = getCity()
                    viewModel.getWeatherReport()
                }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        SearchWeatherReport(viewModel = viewModel)
                    }
                }
            }
        }
    }


    private fun saveCity(city: String) {
        sharedPreference?.edit()?.putString(Constants.KEY_CITY, city)?.apply()
    }

    private fun getCity(): String {
        return sharedPreference?.getString(KEY_CITY, "") ?: ""

    }


    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun SearchWeatherReport(viewModel: WeatherViewModel) {
        val keyboardController = LocalSoftwareKeyboardController.current
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column {
                Text(
                    text = stringResource(R.string.title),
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.purple_700)
                    ),
                    modifier = Modifier
                        .padding(vertical = 24.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                TextField(
                    value = viewModel.city.value,
                    onValueChange = {
                        viewModel.city.value = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .border(BorderStroke(1.dp, Color.DarkGray)),
                    textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        viewModel.getWeatherReport()
                        saveCity(viewModel.city.value)
                        keyboardController?.hide()
                    }),
                    label = {
                        Text(stringResource(R.string.hint))
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White
                    ),
                    singleLine = true
                )
                Box(modifier = Modifier.weight(1f)) {
                    when (viewModel.uiState.value) {
                        is WeatherResponseState.Loading -> {
                            if ((viewModel.uiState.value as WeatherResponseState.Loading).isLoading) {
                                // ShowLoader()
                            }
                        }
                        is WeatherResponseState.OnFailed -> {
                            //ShowError(msg = (viewModel.uiState.value as WeatherResponseState.OnFailed).errorMsg)
                        }
                        else -> {
                            ShowReport(viewModel)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ShowReport(viewModel: WeatherViewModel) {
        val uiData = viewModel.uiState.value as WeatherResponseState.OnSuccess
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberAsyncImagePainter(model = uiData.uiModel.icon),
                    contentDescription = "icon",
                    modifier = Modifier.size(128.dp)
                )
                Column(modifier = Modifier.wrapContentWidth()) {
                    Text(
                        text = uiData.uiModel.temp,
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = uiData.uiModel.title,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            SetWeatherInfo(stringResource(id = R.string.temp, uiData.uiModel.temp))
            SetWeatherInfo(stringResource(id = R.string.desc, uiData.uiModel.description))
            SetWeatherInfo(stringResource(id = R.string.humidity, uiData.uiModel.humidity))
            SetWeatherInfo(stringResource(id = R.string.pressure, uiData.uiModel.pressure))
            SetWeatherInfo(stringResource(id = R.string.wind, uiData.uiModel.windSpeed))
            SetWeatherInfo(stringResource(id = R.string.location, uiData.uiModel.location))
        }
    }

    @Composable
    fun SetWeatherInfo(info: String) {
        Text(
            text = info,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
    }


    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        WeatherAppTheme {
           // val viewModel = hiltViewModel<WeatherViewModel>()
            SearchWeatherReport(viewModel = viewModel)
        }
    }
}