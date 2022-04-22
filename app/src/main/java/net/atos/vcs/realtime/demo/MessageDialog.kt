package net.atos.vcs.realtime.demo

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class MessageDialog(
    private val participantName: String,
    private val participantAddress: String
) : DialogFragment() {

    private var dialogView: View? = null

    // Use this instance of the interface to deliver action events
    var listener: MessageDialogListener? = null

    interface MessageDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, message: String, address: String?)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    // Override to ensure onViewCreated is called
    // see: https://stackoverflow.com/questions/10594992/android-dialogfragment-onviewcreated-not-called
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return dialogView ?: inflater.inflate(R.layout.message_dialog, null)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            dialogView = inflater.inflate(R.layout.message_dialog, null)

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.send,
                    DialogInterface.OnClickListener { _, _ ->
                        listener?.onDialogPositiveClick(this, message(), address())
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { _, _ ->
                        listener?.onDialogNegativeClick(this)
                        getDialog()?.cancel()
                    })

            val title = SpannableString(getText(R.string.send_message).toString())
            title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
            builder.setTitle(title)

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RadioButton>(R.id.participantButton)?.apply {
            text = participantName
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dialogView = null
    }

    private fun address(): String? {
        if (dialogView?.findViewById<RadioButton>(R.id.participantButton)?.isChecked == true) {
            return participantAddress
        }
        return null
    }

    private fun message(): String {
        return dialogView?.findViewById<EditText>(R.id.message)?.text.toString()
    }
}