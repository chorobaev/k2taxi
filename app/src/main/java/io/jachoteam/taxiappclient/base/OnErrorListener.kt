package io.jachoteam.taxiappclient.base

interface OnErrorListener {
    fun onError(error: Throwable)
    fun setErrorMessage(msg: String)
    fun setErrorMessage(msgRes: Int)
}