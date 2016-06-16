package com.hjy.idcardkeyboard.activity;

import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.hjy.idcardkeyboard.R;
import com.hjy.idcardkeyboard.view.CustomKeyboard;
import com.hjy.idcardkeyboard.view.IKeyboardFinish;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    private MaterialEditText edtNum;
    private Context mContext = this;
    private CustomKeyboard mCustomKeyboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtNum = (MaterialEditText) findViewById(R.id.edtNum);
        //屏蔽掉系统默认输入法
        if (Build.VERSION.SDK_INT <= 10) {
            edtNum.setInputType(InputType.TYPE_NULL);
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(edtNum, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //初始化键盘，传入回调接口，以便在此处进行完成后的操作。默认就显示键盘
        KeyboardView keyboardView = (KeyboardView) findViewById(R.id.customKeyboard);
        mCustomKeyboard = new CustomKeyboard(mContext, keyboardView, edtNum, iKeyboardFinish);
        mCustomKeyboard.showKeyboard();
        edtNum.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mCustomKeyboard.showKeyboard();
                return false;
            }
        });
    }

    /**
     * 输入完成
     */
    private IKeyboardFinish iKeyboardFinish = new IKeyboardFinish() {
        @Override
        public void inputFinish() {
            String idcard = edtNum.getText().toString();
            Toast.makeText(mContext,idcard,Toast.LENGTH_SHORT).show();

        }
    };
}
