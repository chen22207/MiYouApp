package com.firstblood.miyo.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import com.firstblood.miyo.R;
import com.firstblood.miyo.util.Navigation;
import com.firstblood.miyo.view.dialog.EditInfoDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class UserInfoCompleteActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE = 1001;
	private static final int REQUEST_CROP_IMAGE = 1002;
	@InjectView(R.id.user_info_header_iv)
    CircleImageView mUserInfoHeaderIv;
    @InjectView(R.id.user_info_header_tv)
    TextView mUserInfoHeaderTv;
    @InjectView(R.id.user_info_nickname_tv)
    TextView mUserInfoNicknameTv;
    @InjectView(R.id.user_info_phone_tv)
    TextView mUserInfoPhoneTv;
    @InjectView(R.id.user_info_QQ_tv)
    TextView mUserInfoQQTv;
    @InjectView(R.id.user_info_username_tv)
    TextView mUserInfoUsernameTv;
    @InjectView(R.id.user_info_sex_tv)
    TextView mUserInfoSexTv;
    @InjectView(R.id.user_info_age_tv)
    TextView mUserInfoAgeTv;
    @InjectView(R.id.user_info_birthplace_tv)
    TextView mUserInfoBirthplaceTv;
    @InjectView(R.id.user_info_address_tv)
    TextView mUserInfoAddressTv;
    @InjectView(R.id.user_info_job_tv)
    TextView mUserInfoJobTv;

    private ArrayList<String> defaultDataArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_complete);
        ButterKnife.inject(this);
        Navigation.getInstance(this).setTitle("完善信息").setBack().setRight("保存", o -> {

        });

    }

    @OnClick({R.id.user_info_header_tv, R.id.user_info_nickname_tv, R.id.user_info_phone_tv, R.id.user_info_QQ_tv, R.id.user_info_username_tv, R.id.user_info_sex_tv, R.id.user_info_age_tv, R.id.user_info_birthplace_tv, R.id.user_info_address_tv, R.id.user_info_job_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_info_header_tv:
                headImgSelect();
                break;
            case R.id.user_info_nickname_tv:
                new EditInfoDialog.Builder(this, getString(R.string.user_nickname), "请输入昵称", mUserInfoNicknameTv.getText().toString(), mUserInfoNicknameTv::setText)
                        .setMaxTextLength(10)
                        .show();
                break;
            case R.id.user_info_phone_tv:
                new EditInfoDialog.Builder(this, getString(R.string.user_phone), "请输入手机号", mUserInfoPhoneTv.getText().toString(), mUserInfoPhoneTv::setText)
                        .setInputType(InputType.TYPE_CLASS_PHONE)
                        .setMaxTextLength(13)
                        .show();
                break;
            case R.id.user_info_QQ_tv:
                break;
            case R.id.user_info_username_tv:
                new EditInfoDialog.Builder(this, getString(R.string.user_name), "请输入真是姓名", "", mUserInfoUsernameTv::setText)
                        .setMaxTextLength(8)
                        .show();
                break;
            case R.id.user_info_sex_tv:
                break;
            case R.id.user_info_age_tv:
                break;
            case R.id.user_info_birthplace_tv:
                break;
            case R.id.user_info_address_tv:
                break;
            case R.id.user_info_job_tv:
                break;
        }
    }

    private void headImgSelect() {
        Intent intent = new Intent(this, MultiImageSelectorActivity.class);
        // whether show camera
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // max select image amount
//        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
        // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
	    intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
	    // default select images (support array list)
        intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, defaultDataArray);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                // Get the result list of select image paths
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // do your logic ....
	            Intent intent = new Intent(this, CropImageActivity.class);
	            intent.putExtra("path", path.get(0));
	            startActivityForResult(intent, REQUEST_CROP_IMAGE);
            }
        }
    }
}
