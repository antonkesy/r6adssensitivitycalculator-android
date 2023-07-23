import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.poorskill.r6adssensitivitycalculator.R

fun copyValueToClipboard(value: String, name: String, context: Context) {
  val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
  val clip = ClipData.newPlainText(context.getString(R.string.copyValues), value)
  clipboard.setPrimaryClip(clip)
  Toast.makeText(
          context,
          context.getString(R.string.copied) + name + context.getString(R.string.toClipboard),
          Toast.LENGTH_SHORT
      )
      .show()
}

fun clearFocus(
    activity: Activity
) {
  activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
  // https://stackoverflow.com/questions/1109022/how-do-you-close-hide-the-android-soft-keyboard-programmatically
  activity.currentFocus?.let { view ->
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
  }
}
