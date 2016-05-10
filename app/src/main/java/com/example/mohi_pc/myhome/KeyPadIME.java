package com.example.mohi_pc.myhome;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.Keyboard;
import android.view.View;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;

/**
 * Created by Xarrin on 5/4/2016.
 */
public class KeyPadIME extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener{

    private KeyboardView kv;
    private Keyboard keyboard;

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        //kv.set
        return kv;
    }

    private void playClick(int keyCode){
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch(keyCode){
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default: am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

   @Override
    public void onKey(int primaryCode, int[] keyCodes) {
       InputConnection ic = getCurrentInputConnection();
       playClick(primaryCode);
       switch(primaryCode){
           case Keyboard.KEYCODE_DELETE :
               ic.deleteSurroundingText(1, 0);
               break;
           case Keyboard.KEYCODE_DONE:
               ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
               break;
           default:
               char code = (char)primaryCode;
               ic.commitText(String.valueOf(code),1);
       }
    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {
    }
}
