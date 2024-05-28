package com.capstone.ccal.ui.login

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentRecomposeScope
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
import androidx.compose.ui.platform.LocalContext
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
import com.capstone.ccal.ui.component.CircularImage
import com.capstone.ccal.ui.component.ColumnTitleText
import com.capstone.ccal.ui.component.ProgressWithText
import com.capstone.ccal.ui.component.RoundTextButton
import com.capstone.ccal.ui.component.TitleText
import com.capstone.ccal.ui.navigation.MainDestination
import com.capstone.ccal.ui.theme.DeepSkyBlue
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

@Composable
fun Register(
    onDetailClick: (String) -> Unit,
    onNavigateToRoute: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val registerViewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModel.provideFactory(RegisterRepository())
    )

    val currentStep by registerViewModel.currentStep // 단계별 상태 값

    val animationState = remember { mutableStateOf(false) }
    val offset by animateDpAsState(
        targetValue = if (animationState.value) (0).dp else 0.dp,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing), label = ""
    )

    val titleOffset by animateDpAsState(
        targetValue = if (animationState.value) (0).dp else 0.dp,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing), label = ""
    )

    val bodyAlpha by animateFloatAsState(
        targetValue = if (animationState.value) 1f else 1f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing), label = ""
    )

    //버퍼링 프로세스
    val isLoading by registerViewModel.loadingProgressState
    if (isLoading) {
        ProgressWithText(
            color = MaterialTheme.colorScheme.primary
        )
    }

    val registerSuccess by registerViewModel.registerResult.observeAsState()
    if (registerSuccess == true) {
        Log.d("seki", "register Success?")
        onNavigateToRoute(MainDestination.LOGIN, false)
    }

    val errorOn by registerViewModel.errorMessageOn
    val errorMessage by registerViewModel.messageResult
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleText(
            text = stringResource(id = R.string.register_register),
            modifier = Modifier
                .padding(top = titleOffset)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Header(
            viewModel = registerViewModel,
            modifier = Modifier
                .background(Color.Transparent)
                .align(Alignment.CenterHorizontally)
                .padding(top = offset))
//            .clickable {
//                animationState.value = !animationState.value
//            })


        Spacer(modifier = Modifier.height(24.dp))

        Body(
            viewModel = registerViewModel,
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
    viewModel: RegisterViewModel,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularImage(
            drawableId = R.drawable.book_opened,
            size = 100.dp
        )

        TitleText(text = stringResource(id = R.string.login_title))

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun Body(
    viewModel: RegisterViewModel,
    animationState: Boolean,
    onNavigateToRoute: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        val emailHint = stringResource(id = R.string.login_email_hint)
        val passHint = stringResource(id = R.string.login_pass_hint)
        val nicknameHint = stringResource(id = R.string.register_name)

        var emailInputText by rememberSaveable { mutableStateOf("") }
        var passwordInputText by rememberSaveable { mutableStateOf("") }
        var nickNameInputText by rememberSaveable { mutableStateOf("") }

        val currentStep by viewModel.currentStep // 단계별 상태 값
        val checkDuplicated by viewModel.checkDuplicatedState
        val duplicated by viewModel.duplicatedRes.observeAsState()

        val errorOn by remember { mutableStateOf(false) }
        val errorMessage by remember { mutableStateOf("") }
        val messageAlpha by animateFloatAsState(
            targetValue = if (errorOn) 1f else 0f,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing), label = ""
        )
        if (errorOn) {
            AlertSnackBar(
                text = errorMessage,
                errorOn = errorOn,
                modifier = Modifier
                    .alpha(messageAlpha)
                    .padding(16.dp)
            )
        }

        if (!animationState) {
            LazyColumn(
              modifier = Modifier
                  .background(
                      color = MaterialTheme.colorScheme.onBackground,
                      shape = RoundedCornerShape(
                          topStart = 32.dp,
                          topEnd = 32.dp
                      )
                  )
                  .fillMaxSize()
            ) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))

                    ColumnTitleText(
                        text = stringResource(id = R.string.register_email),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                end = 16.dp
                            )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row {
                        TextField(
                            value = emailInputText,
                            placeholder = { ColumnTitleText(emailHint) },
                            onValueChange = { inputEmail -> emailInputText = inputEmail },
                            label = { Text(stringResource(id = R.string.register_email_desc)) },
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
                            modifier = Modifier
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp
                                )
                                .fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done // 엔터 키를 눌렀을 때 완료(키보드 숨기기) 동작을 합니다
                            ),
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    ColumnTitleText(
                        text = stringResource(id = R.string.register_password),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                end = 16.dp
                            )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = passwordInputText,
                        placeholder = { ColumnTitleText(passHint) },
                        onValueChange = { inputPassword ->
                            passwordInputText = inputPassword
                        },
                        label = { Text(stringResource(id = R.string.register_password_desc)) },
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
//                        keyboardOptions = KeyboardOptions.Default.copy(
//                            imeAction = ImeAction.Done // 엔터 키를 눌렀을 때 완료(키보드 숨기기) 동작을 합니다
//                        ),
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                end = 16.dp
                            )
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ColumnTitleText(
                        text = stringResource(id = R.string.register_nickname),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                end = 16.dp
                            )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = nickNameInputText,
                        placeholder = { ColumnTitleText(nicknameHint) },
                        onValueChange = { inputNickname ->
                            nickNameInputText = inputNickname
                        },
                        label = { Text(stringResource(id = R.string.register_nickname)) },
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
                            .padding(
                                start = 16.dp,
                                end = 16.dp
                            )
                            .fillMaxWidth()
                    )

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (checkDuplicated && duplicated == false) {
                            RegisterButton(
                                text = stringResource(id = R.string.register_register)
                            ) {
                                viewModel.setEmailAndPassword(emailInputText, passwordInputText)
                                viewModel.setProfile(nickNameInputText)
                                viewModel.register()
                            }
                        } else {
                            RegisterButton(
                                text = stringResource(id = R.string.register_duplicate_check)
                            ) {
                                viewModel.setEmailAndPassword(emailInputText, passwordInputText)
                                viewModel.setProfile(nickNameInputText)
                                viewModel.checkDuplicated(emailInputText)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ImagePicker(
    onImageLoaded: (Uri) -> Unit
) {
    val context = LocalContext.current
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            onImageLoaded(it)
        }
    }

    Button(onClick = {
        pickImageLauncher.launch("image/*")
    }) {
        Text(text = "이미지 선택")
    }
}

@Composable
private fun DuplicateCheckButton(
    isChecked: Boolean,
    onClick: () -> Unit,
) {
    val checkedText = if (isChecked) stringResource(id = R.string.register_duplicate_check_complete)
    else stringResource(id = R.string.register_duplicate_check)

    RoundTextButton(
        mainText = checkedText,
        onClick = {
            if (!isChecked) { onClick() }
        },
        modifier = Modifier
            .padding(8.dp)
            .wrapContentWidth()
            .background(MaterialTheme.colorScheme.primary)
    )
}

@Composable
private fun DuplicateCheckCompleteButton(
    onClick: () -> Unit,
) {
    RoundTextButton(
        mainText = stringResource(id = R.string.register_duplicate_check_complete),
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .wrapContentWidth()
//            .background(Color.LightGray)
    )
}

@Composable
private fun NextStepButton(
    onClick: () -> Unit
) {
    RoundTextButton(
        mainText = stringResource(id = R.string.register_next_button),
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .wrapContentWidth()
    )
}

@Composable
private fun PrevStepButton(
    onClick: () -> Unit
) {
    RoundTextButton(
        mainText = stringResource(id = R.string.register_prev_button),
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .wrapContentWidth()
    )
}

@Composable
private fun RegisterButton(
    text: String = stringResource(id = R.string.register_duplicate_check),
    onClick: () -> Unit
) {
    RoundTextButton(
        mainText = text,
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp)
            .wrapContentWidth()
    )
}