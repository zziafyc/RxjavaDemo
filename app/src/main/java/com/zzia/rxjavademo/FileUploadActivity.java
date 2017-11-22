package com.zzia.rxjavademo;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.yalantis.ucrop.entity.LocalMedia;
import com.zzia.rxjavademo.adapter.GridImageAdapter;
import com.zzia.rxjavademo.base.ApiClient;
import com.zzia.rxjavademo.base.BaseActivity;
import com.zzia.rxjavademo.base.HttpResult;
import com.zzia.rxjavademo.utils.FullyGridLayoutManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FileUploadActivity extends BaseActivity {
    @Bind(R.id.as_pics_rv)
    RecyclerView picsRecyclerView;
    @Bind(R.id.ast_cancel_tv)
    TextView cancelTv;
    @Bind(R.id.ast_send_tv)
    TextView sendTv;
    @Bind(R.id.ast_content_edit)
    EditText contentEdt;

    GridImageAdapter mAdapter;
    public static final int REQUEST_LOCATION = 18;
    private int selectMode = FunctionConfig.MODE_MULTIPLE; //多选模式
    private int maxSelectNum = 9;// 图片最大可选数量
    private int selectType = FunctionConfig.TYPE_IMAGE;
    private boolean enablePreview = true;
    private boolean isPreviewVideo = true;
    private boolean isShow = true;
    private boolean enableCrop = false;
    private boolean theme = false;
    private boolean selectImageType = false;
    private boolean isCheckNumMode = false;
    private int themeStyle;
    private int previewColor;
    private int completeColor;
    private int checkedBoxDrawable;  //图片选择的checkbox风格
    private int previewBottomBgColor;  //预览底部颜色
    private int bottomBgColor;
    private List<LocalMedia> selectMedia = new ArrayList<>();  //已选的
    int k = 0;//标记已上传的图片数
    private List<PhotoModel> mPhotoList = new ArrayList<>();
    private static final String TAG = "SendTrendActivity";


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_file_upload;
    }


    @Override
    public void initViewsAndEvents() {
        initViews();
        initAdapter();
        initListener();

    }

    private void initViews() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(FileUploadActivity.this, 4, GridLayoutManager.VERTICAL, false);
        picsRecyclerView.setLayoutManager(manager);
        //隐藏键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }

    }

    private void initAdapter() {
        mAdapter = new GridImageAdapter(FileUploadActivity.this, onAddPicClickListener);
        mAdapter.setList(selectMedia);
        mAdapter.setSelectMax(9);
        picsRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                switch (selectType) {
                    case FunctionConfig.TYPE_IMAGE:
                        // 预览图片
                        PictureConfig.getInstance().externalPicturePreview(FileUploadActivity.this, position, selectMedia);
                        break;
                    case FunctionConfig.TYPE_VIDEO:
                        // 预览视频
                        if (selectMedia.size() > 0) {
                            PictureConfig.getInstance().externalPictureVideo(FileUploadActivity.this, selectMedia.get(position).getPath());
                        }
                        break;
                }

            }
        });
    }

    private void initListener() {
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FileUploadActivity.this,"请稍等....",Toast.LENGTH_LONG).show();

                if (selectMedia.size() == 1) {
                    //上传单张图片
                    uploadOneFile(selectMedia.get(0).getPath());
                } else {
                    //上传多张图片
                    List<File> files = new ArrayList<>();
                    for (LocalMedia media : selectMedia) {
                        files.add(new File(media.getPath()));
                    }
                    uploadMultiFile(files);
                }

            }
        });
    }

    public void uploadOneFile(String path) {
        //单个文件上传，也可以通过多文件上传partMap的方式
        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        ApiClient.getApiService().uploadOnePic(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResult<Void>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());

                    }

                    @Override
                    public void onNext(HttpResult<Void> result) {
                        Toast.makeText(FileUploadActivity.this,"单个文件上传成功",Toast.LENGTH_LONG).show();
                        Log.e(TAG, "单个文件上传:" + result.getMessage() + " " + result.getResultCode());

                    }
                });
    }

    public void uploadMultiFile(List<File> files) {
        //组装partMap对象
        Map<String, RequestBody> partMap = new HashMap<>();
        for (File file : files) {
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
            String fileName = "image_" + System.currentTimeMillis() + "." + file.getPath().split("\\.")[1];
            partMap.put("file\"; filename=\"" + fileName + "\"", fileBody);
        }
        ApiClient.getApiService().uploadMultiPic(partMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResult<Void>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());

                    }

                    @Override
                    public void onNext(HttpResult<Void> result) {
                        Toast.makeText(FileUploadActivity.this,"多个文件上传成功",Toast.LENGTH_LONG).show();
                        Log.e(TAG, "多个文件上传:" + result.getMessage() + " " + result.getResultCode());

                    }
                });
    }

    /**
     * 添加删除图片回调接口
     */

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick(int type, int position) {
            switch (type) {
                case 0:
                    if (theme) {
                        // 设置主题样式
                        themeStyle = ContextCompat.getColor(getApplicationContext(), R.color.blue);
                    } else {
                        themeStyle = ContextCompat.getColor(getApplicationContext(), R.color.bar_grey);
                    }
                    if (isCheckNumMode) {
                        // QQ 风格模式下 这里自己搭配颜色
                        previewColor = ContextCompat.getColor(getApplicationContext(), R.color.blue);
                        completeColor = ContextCompat.getColor(getApplicationContext(), R.color.blue);
                    } else {
                        previewColor = ContextCompat.getColor(getApplicationContext(), R.color.tab_color_true);
                        completeColor = ContextCompat.getColor(getApplicationContext(), R.color.tab_color_true);
                    }
                    if (selectImageType) {
                        checkedBoxDrawable = R.drawable.select_cb;
                    } else {
                        checkedBoxDrawable = 0;
                    }

                    FunctionOptions options = new FunctionOptions.Builder()
                            .setType(selectType) // 图片or视频 FunctionConfig.TYPE_IMAGE  TYPE_VIDEO
                            .setCompress(false) //是否压缩
                            .setEnablePixelCompress(true) //是否启用像素压缩
                            .setEnableQualityCompress(true) //是否启质量压缩
                            .setMaxSelectNum(maxSelectNum) // 可选择图片的数量
                            .setSelectMode(selectMode) // 单选 or 多选
                            .setShowCamera(isShow) //是否显示拍照选项 这里自动根据type 启动拍照或录视频
                            .setEnablePreview(enablePreview) // 是否打开预览选项
                            .setEnableCrop(enableCrop) // 是否打开剪切选项
                            .setPreviewVideo(isPreviewVideo) // 是否预览视频(播放) mode or 多选有效
                            .setCheckedBoxDrawable(checkedBoxDrawable)
                            .setRecordVideoDefinition(FunctionConfig.HIGH) // 视频清晰度
                            .setRecordVideoSecond(60) // 视频秒数
                            .setGif(false)// 是否显示gif图片，默认不显示
                            .setPreviewColor(previewColor) //预览字体颜色
                            .setCompleteColor(completeColor) //已完成字体颜色
                            .setPreviewBottomBgColor(previewBottomBgColor) //预览底部背景色
                            .setBottomBgColor(bottomBgColor) //图片列表底部背景色
                            .setGrade(Luban.THIRD_GEAR) // 压缩档次 默认三档
                            .setCheckNumMode(isCheckNumMode)
                            .setCompressQuality(100) // 图片裁剪质量,默认无损
                            .setImageSpanCount(3) // 每行个数
                            .setSelectMedia(selectMedia) // 已选图片，传入在次进去可选中，不能传入网络图片
                            .setThemeStyle(themeStyle) // 设置主题样式
                            .create();
                    // 先初始化参数配置，在启动相册
                    PictureConfig.getInstance().init(options).openPhoto(FileUploadActivity.this, resultCallback);

                    break;
                case 1:
                    // 删除图片
                    selectMedia.remove(position);
                    mAdapter.notifyItemRemoved(position);
                    break;
            }
        }
    };


    /**
     * 图片回调方法
     */
    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {
            selectMedia = resultList;
            Log.i("callBack_result", selectMedia.size() + "");
            LocalMedia media = resultList.get(0);
            if (media.isCut() && !media.isCompressed()) {
                // 裁剪过
                String path = media.getCutPath();
            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                String path = media.getCompressPath();
            } else {
                // 原图地址
                String path = media.getPath();
            }
            if (selectMedia != null) {
                mAdapter.setList(selectMedia);
                mAdapter.notifyDataSetChanged();
            }
        }
    };
}
