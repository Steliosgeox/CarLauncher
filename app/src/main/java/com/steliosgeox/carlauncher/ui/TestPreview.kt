package com.steliosgeox.carlauncher.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TestComposable() {
    Text(text = "Hello World")
}

@Preview
@Composable
fun MyTestPreview() {
    TestComposable()
}
