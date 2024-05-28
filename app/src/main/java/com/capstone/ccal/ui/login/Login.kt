package com.capstone.ccal.ui.login

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.ccal.CalApplication
import com.capstone.ccal.R
import com.capstone.ccal.ui.component.AlertSnackBar
import com.capstone.ccal.ui.component.BlinkingText
import com.capstone.ccal.ui.component.CircularImage
import com.capstone.ccal.ui.component.ColumnTitleText
import com.capstone.ccal.ui.component.ProgressWithText
import com.capstone.ccal.ui.component.RoundTextButton
import com.capstone.ccal.ui.component.TitleText
import com.capstone.ccal.ui.home.HomeSections
import com.capstone.ccal.ui.navigation.MainDestination
import com.capstone.ccal.ui.theme.DeepSkyBlue
import com.capstone.ccal.ui.theme.PastelGreen
import com.capstone.ccal.ui.theme.PastelGreenLight
import kotlin.system.exitProcess

@Composable
fun Login(
    onDetailClick: (String) -> Unit,
    onNavigateToRoute: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var exit by remember { mutableStateOf(false) }
    var exitPressedTime by remember { mutableStateOf(0L) }
    val exitString = stringResource(id = R.string.common_confirm_exit)
    BackHandler { // 백프레스 이벤트
        if (exit) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - exitPressedTime < 2000) {
                exitProcess(0)
            } else {
                Toast.makeText(CalApplication.ApplicationContext(), exitString, Toast.LENGTH_SHORT).show()
                exitPressedTime = currentTime
            }
        } else {
            exit = true
            exitPressedTime = System.currentTimeMillis()
            Toast.makeText(CalApplication.ApplicationContext(), exitString, Toast.LENGTH_SHORT).show()
        }
    }
    val userViewModel: UserViewModel = viewModel(factory = UserViewModel.provideFactory())

    //버퍼링 프로세스
    val isLoading by userViewModel.loadingProgressState.observeAsState()
    if (isLoading == true) {
        ProgressWithText(
            text = stringResource(id = R.string.login_description),
            color = MaterialTheme.colorScheme.primary
        )
    }

    //홈으로 이동
    val loginSuccess by userViewModel.loginResult.observeAsState()
    if (loginSuccess == true) {
        Log.d("seki", "login Success?")
        onNavigateToRoute(HomeSections.FEED.route, false)
    }

    val errorOn by userViewModel.messageState
    val errorMessage by userViewModel.stringResult
    val messageAlpha by animateFloatAsState(
        targetValue = if (errorOn) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing), label = ""
    )

    if (errorOn) { // 상태에 따라나 스낵바가 나타나도록 함
//        Log.d("seki", "Error Occured message : $errorMessage")
        AlertSnackBar(
            text = errorMessage,
            errorOn = errorOn,
            modifier = Modifier
                .alpha(messageAlpha)
                .padding(16.dp)
                .zIndex(1f)
        )
    }

    val animationState = rememberSaveable { mutableStateOf(false) }
    val offset by animateDpAsState(
        targetValue = if (animationState.value) (0).dp else 100.dp,
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing), label = ""
    )

    val titleOffset by animateDpAsState(
        targetValue = if (animationState.value) (0).dp else 100.dp,
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing), label = ""
    )

    val bodyAlpha by animateFloatAsState(
        targetValue = if (animationState.value) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing ), label = ""
    )

//    RisingView()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
//            .drawBehind {
//                repeat(1) {
//                    val x = Random()
//                        .nextInt(size.width.toInt())
//                        .toFloat()
//                    val y = Random()
//                        .nextInt(size.height.toInt())
//                        .toFloat()
//                    val radius = Random().nextFloat() * 50f
//                    drawCircle(Color.White, radius, Offset(x, y))
//                }
//            }
            .clickable {
                animationState.value = !animationState.value
            },
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        TitleText(
            text = if (animationState.value) stringResource(id = R.string.login_login)
            else stringResource(id = R.string.login_welcome),
            modifier = Modifier
                .padding(top = titleOffset)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Header(
            animationState = animationState.value,
            modifier = Modifier
//            .background(Color.Black)
                .align(Alignment.CenterHorizontally)
                .padding(top = offset)
        )

        Body(
            viewModel = userViewModel,
            animationState = animationState.value,
            onNavigateToRoute,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(bodyAlpha)
                .padding(top = offset)
        )
    }
}

@Composable
private fun Header(
    animationState: Boolean,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .background(Color.Transparent),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        CircularImage(
            drawableId = if (!animationState) R.drawable.logo_white else R.drawable.logo_white,
            size = 240.dp,
        )

//        TitleText(text = stringResource(id = R.string.login_title))

//        Spacer(modifier = Modifier.height(16.dp))

        if (!animationState) {
            BlinkingText(
                text = stringResource(id = R.string.login_touch_screen),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Body(
    viewModel: UserViewModel,
    animationState: Boolean,
    onNavigateToRoute: (String, Boolean) -> Unit,
//    onNavigateToRouteBackStack: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(
                top = 24.dp,
            )
            .background(
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(
                    topStart = 32.dp,
                    topEnd = 32.dp
                ),
            )
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val emailHint = stringResource(id = R.string.login_email_hint)
        val passHint = stringResource(id = R.string.login_pass_hint)
        var emailInputText by remember { mutableStateOf("") }
        var passwordInputText by remember { mutableStateOf("") }

        if (animationState) {
            Column(
                modifier = Modifier
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    ColumnTitleText(
                        text = stringResource(id = R.string.login_email),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth(0.2f)
                    )

                    TextField(
                        placeholder = { ColumnTitleText(emailHint) },
                        value = emailInputText,
                        onValueChange = { inputEmail -> emailInputText = inputEmail },
//                        label = { Text(stringResource(id = R.string.login_email)) },
                        colors = TextFieldDefaults.colors(
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                            focusedContainerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            focusedTextColor = MaterialTheme.colorScheme.primary,
                            unfocusedTextColor = MaterialTheme.colorScheme.primary
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done // 엔터 키를 눌렀을 때 완료(키보드 숨기기) 동작을 합니다
                        ),
                        modifier = Modifier
                    )

                    Spacer(modifier = Modifier.width(24.dp))
                }
                
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ColumnTitleText(
                        text = stringResource(id = R.string.login_pass),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth(0.2f)
                    )

                    TextField(
                        placeholder = { ColumnTitleText(passHint) },
                        value = passwordInputText,
                        onValueChange = { inputPassword -> passwordInputText = inputPassword },
//                        label = { Text(stringResource(id = R.string.login_pass)) },
                        colors = TextFieldDefaults.colors(
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.primary,
                            focusedContainerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            focusedTextColor = MaterialTheme.colorScheme.primary,
                            unfocusedTextColor = MaterialTheme.colorScheme.primary
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LoginButton(
                        onClick = {
//                            viewModel.logIn(emailInputText, passwordInputText)
                            onNavigateToRoute(HomeSections.FEED.route, false)
                        }
                    )

                    RegisterButton(
                        onClick = {
//                        onNavigateToRouteBackStack(MainDestination.REGISTER)
                            onNavigateToRoute(MainDestination.REGISTER, true)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LoginButton(
    onClick: () -> Unit
) {
    RoundTextButton(
        mainText = stringResource(id = R.string.login_login),
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .wrapContentWidth()
    )
}

@Composable
private fun RegisterButton(
    onClick: () -> Unit
) {
    RoundTextButton(
        mainText = stringResource(id = R.string.register_register),
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .wrapContentWidth()
    )
}