package hu.landov.pdpfront

import android.content.Intent

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import hu.landov.pdpfront.ui.theme.PdpfrontTheme
import java.io.OutputStream

const val REQUEST_CREATE_PDF_FILE = 1
const val TYPE_APPLICATION_PDF = "application/pdf"

class MainActivity : ComponentActivity() {

    private val viewModel: PrintViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val formState by viewModel.formState

            PdpfrontTheme {
                // A surface container using the 'background' color from the theme
                Form(formState, ::print, viewModel)

            }
        }
    }

    private fun print() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = TYPE_APPLICATION_PDF
            putExtra(
                Intent.EXTRA_TITLE,
                "pdp_front.pdf"
            )
        }
        startActivityForResult(intent, REQUEST_CREATE_PDF_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data?.data != null) {
            when (requestCode) {
                REQUEST_CREATE_PDF_FILE -> {
                    val outputStream: OutputStream? = contentResolver.openOutputStream(data.data!!)
                    if (outputStream != null) {
                        try {
                            viewModel.print(outputStream)
                            val intent = Intent()
                            intent.action = Intent.ACTION_VIEW
                            intent.setDataAndType(data.data, TYPE_APPLICATION_PDF)
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(
                                this,
                                "Pdf generation failsed: ${e.message}", Toast.LENGTH_SHORT
                            )
                                .show()
                        } finally {
                            outputStream.close()
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Form(
    formState: FormState,
    printform: () -> Unit,
    viewModel: PrintViewModel,
) {

    var redText = formState.redColor
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,

            ) {
            Text("PDP-11 Sticker Generator")
            ColorRow(
                color = Color(android.graphics.Color.parseColor(formState.redColor)),
                label = "Red",
                initialText = formState.redColor,
                setColor = viewModel::setRed
            )
            ColorRow(
                color = Color(android.graphics.Color.parseColor(formState.purpleColor)),
                label = "Purple",
                initialText = formState.purpleColor,
                setColor = viewModel::setPurple
            )
            ColorRow(
                color = Color(android.graphics.Color.parseColor(formState.backColor)),
                label = "Background",
                initialText = formState.backColor,
                setColor = viewModel::setBack

            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    onClick = viewModel::decNumber,
                    enabled = formState.stickerNumber > 1

                ) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_remove_24),
                        contentDescription = null
                    )
                }
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = formState.stickerNumber.toString(),
                    style = MaterialTheme.typography.headlineLarge
                )
                OutlinedButton(
                    onClick = viewModel::incNumber,
                    enabled = formState.stickerNumber < 3) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_add_24),
                        contentDescription = null
                    )
                }
            }


            Button(
                onClick = printform
            ) {
                Text("Print")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorRow(
    color: Color,
    label: String,
    initialText: String,
    setColor: (String) -> Unit,
) {
    var text by remember { mutableStateOf(initialText) }
    var isValid by remember { mutableStateOf(validColor(initialText)) }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(120.dp).padding(16.dp).clip(CircleShape)
                .background(color)
        )
        OutlinedTextField(
            value = text,
            onValueChange =
            {
                text = it
                isValid = validColor(it)
                if (isValid) setColor(it)
            },
            label = { Text(label) },
            isError = !isValid,
            supportingText = { Text(if (!isValid) "Wrong color string" else "") },
            singleLine = true
        )
    }
}



