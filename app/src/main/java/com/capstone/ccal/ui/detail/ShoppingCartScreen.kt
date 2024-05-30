package com.capstone.ccal.ui.detail

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.ccal.CalApplication
import com.capstone.ccal.R
import com.capstone.ccal.model.BookDetailItem
import com.capstone.ccal.ui.component.AlertSnackBar
import com.capstone.ccal.ui.component.BookImageRound
import com.capstone.ccal.ui.component.ColumnTitleText
import com.capstone.ccal.ui.component.HorizontalItemDivider
import com.capstone.ccal.ui.component.ProgressWithText
import com.capstone.ccal.ui.login.RegisterRepository
import com.capstone.ccal.ui.theme.customFont
import com.capstone.ccal.ui.util.mirroringBackIcon
import kotlin.system.exitProcess

private val TitleHeight = 128.dp
private val HeaderHeight = 56.dp
private val horizontalPv = 12.dp
private val BottomBarHeight = 56.dp

/**
 * 상품 구매 팝업
 */
@Composable
fun ShoppingCartScreen(
    bookDetailItem: BookDetailItem, // 책 정보
    onDismiss: () -> Unit, //닫기
    modifier: Modifier = Modifier
) {

    val viewModel: ShoppingCartViewModel = viewModel(factory = ShoppingCartViewModel.provideFactory(
        RegisterRepository()
    ))

    //버퍼링 프로세스
    val isLoading by viewModel.loadingProgressState
    if (isLoading) {
        Log.d("seki", "Loading Start")
        ProgressWithText(
            color = MaterialTheme.colorScheme.primary,
        )
    }

    val errorOn by viewModel.messageState
    val errorMessage by viewModel.stringResult
    val messageAlpha by animateFloatAsState(
        targetValue = if (errorOn) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing), label = ""
    )

    if (errorOn) { // 상태에 따라나 스낵바가 나타나도록 함
        AlertSnackBar(
            text = errorMessage,
            errorOn = errorOn,
            modifier = Modifier
                .alpha(messageAlpha)
                .padding(16.dp)
                .zIndex(1f)
        )
    }

    val dismiss by viewModel.dismissState

    if (dismiss) {
        onDismiss()
    }

//    Dialog(
//        properties = DialogProperties(usePlatformDefaultWidth = false),
//        onDismissRequest = onDismiss,
//    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            val scroll = rememberScrollState(0)
            Header()
            Body(
                vm = viewModel,
                scroll = scroll,
                bookDetailItem = bookDetailItem,
                modifier = Modifier.padding(
                    horizontal = horizontalPv,
                    )
            )
            Up(onDismiss)

            ShoppingCartBottomBar(
                modifier
                    .align(Alignment.BottomCenter)
                    .clickable {
                        viewModel.updateInfo(bookDetailItem)
                    }
            )
        }
    }
//}

@Composable
private fun ShoppingCartBottomBar(
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .height(BottomBarHeight)
            .background(MaterialTheme.colorScheme.onBackground)
            .fillMaxWidth()
        ,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.detail_shopping_purchase),
            fontFamily = customFont,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Icon(
            imageVector = Icons.Default.PlayArrow,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = ""
        )
    }
}

@Composable
private fun Body(
    vm: ShoppingCartViewModel,
    bookDetailItem: BookDetailItem,
    scroll: ScrollState,
    modifier: Modifier = Modifier
) {

    val imageUrl = bookDetailItem.bookImageUrl
    //아이템 설명 영역

    Column(modifier = modifier) {
        Spacer(
            modifier = modifier
                .fillMaxWidth()
//                .statusBarsPadding()
                .height(HeaderHeight)
        )

        Column(
            modifier = modifier
                .verticalScroll(scroll)
                .fillMaxSize()
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = modifier) {
                Column(
                    modifier = Modifier
                        .width(120.dp)
                        .height(160.dp)
                ) {
                    BookImageRound(
                        imageUrl = imageUrl,
                        contentDescription = null,
                        modifier = modifier,
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column() {
                    ColumnTitleText(
                        text = bookDetailItem.bookName,
                        modifier = Modifier
                            .align(Alignment.Start))
                    Spacer(modifier = Modifier.height(4.dp))
                    ColumnTitleText(
                        text = "${bookDetailItem.price} Point",
                        modifier = Modifier
                            .align(Alignment.Start))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalItemDivider()

            val addressHint = stringResource(id = R.string.detail_shopping_address_hint)
            val addNumberHint = stringResource(id = R.string.detail_shopping_address_number)
            val phoneNumberHint = stringResource(id = R.string.detail_shopping_phone)
            val memoHint = stringResource(id = R.string.detail_shopping_memo_hint)
            val nameHint = stringResource(id = R.string.detail_shopping_name)

            Spacer(modifier = Modifier.height(8.dp))

            ColumnTitleText( //주소지
                text = stringResource(id = R.string.detail_shopping_address),
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = vm.address.value,
                placeholder = addressHint,
                label = stringResource(id = R.string.detail_shopping_address_hint),
                onValueChange = { inputText -> vm.address.value = inputText },
            )

            Spacer(modifier = Modifier.height(8.dp))

            ColumnTitleText( //우편번호
                text = stringResource(id = R.string.detail_shopping_address_number),
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = vm.addressNum.value,
                placeholder = addNumberHint,
                label = stringResource(id = R.string.detail_shopping_address_hint),
                onValueChange = { inputText -> vm.addressNum.value = inputText },
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(8.dp))

            ColumnTitleText( //수취인
                text = stringResource(id = R.string.detail_shopping_name),
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = vm.userName.value,
                placeholder = nameHint,
                label = stringResource(id = R.string.detail_shopping_name),
                onValueChange = { inputText -> vm.userName.value = inputText },
            )

            Spacer(modifier = Modifier.height(8.dp))

            ColumnTitleText( //전화번호
                text = stringResource(id = R.string.detail_shopping_phone),
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = vm.phone.value,
                placeholder = phoneNumberHint,
                label = stringResource(id = R.string.detail_shopping_phone_hint),
                onValueChange = { inputText -> vm.phone.value = inputText },
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(8.dp))

            ColumnTitleText( //배송 메모
                text = stringResource(id = R.string.detail_shopping_memo),
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = vm.deliveryMemo.value,
                placeholder = memoHint,
                label = stringResource(id = R.string.detail_shopping_memo_hint),
                onValueChange = { inputText -> vm.deliveryMemo.value = inputText },
                modifier = Modifier.heightIn(min = 150.dp)
            )

            Spacer(modifier = modifier.height(BottomBarHeight + 16.dp))

        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    placeholder: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    imeAction: ImeAction = ImeAction.Done,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    TextField(
        value = value,
        placeholder = { ColumnTitleText(placeholder) },
        onValueChange = onValueChange,
        label = { Text(label) },
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
        maxLines = maxLines,
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = keyboardType
        )
    )
}


//상단 헤더
@Composable
private fun Header(

) {
    Row(
        modifier = Modifier
            .height(HeaderHeight)
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.onBackground
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ColumnTitleText(
            text = "배송지 입력하기",
        )
    }
}

@Composable
private fun Up(upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .size(36.dp)
            .background(
                color = MaterialTheme.colorScheme.background.copy(alpha = 0.4f),
                shape = CircleShape
            )
            .zIndex(5f)
    ) {
        Icon(
            imageVector = mirroringBackIcon(),
            tint = MaterialTheme.colorScheme.surfaceTint,
            contentDescription = stringResource(R.string.back_description)
        )
    }
}
