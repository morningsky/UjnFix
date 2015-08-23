package firefly.ujnfix.user.view;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import firefly.ujnfix.R;

/**
 * Created by Phineas on 2015/7/28.
 */
public class PhotoFragment extends Fragment {

    private ImageView iv_camera;
    private Button btn_cancel;
    private Button btn_camera;
    private Button btn_choosePhoto;
    private  static Uri imageUri;
    private  static File imageFile;
    private  static ImageView iv_first;
    private  static ImageView iv_second;
    private  static ImageView iv_third;
    public  static ArrayList<String> bitmap_uri_submit = new ArrayList<String>();//全局类变量 一定要初始化
    private static Bitmap bitmap_submit;
    private View   view_dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iv_first = new ImageView(getActivity());
        iv_second=new ImageView(getActivity());
        iv_third=new ImageView(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //加载照相机视图
        View view = inflater.inflate(R.layout.fragment_photo,container,false);
        iv_second = (ImageView) view.findViewById(R.id.iv_second);
        iv_first = (ImageView) view.findViewById(R.id.iv_fist);
        iv_third = (ImageView) view.findViewById(R.id.iv_third);
        iv_camera = (ImageView) view.findViewById(R.id.iv_camera);


        iv_camera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        return view;
    }

    public void showDialog()
    {
        //加载自定义对话框中视图
        view_dialog =  getActivity().getLayoutInflater().inflate(R.layout.dialog_selectphoto,null);
        //实列化对话框中的三大按钮
        btn_cancel = (Button) view_dialog.findViewById(R.id.btn_cancel);
        btn_camera = (Button) view_dialog.findViewById(R.id.btn_camera);
        btn_choosePhoto = (Button) view_dialog.findViewById(R.id.btn_choosePhoto);
        //仿QQ空间发说说选照片
        final Dialog dialog = new Dialog(getActivity(),R.style.transparentFrameWindowStyle);
        dialog.setContentView(view_dialog,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.x = 0;
        wl.y = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        dialog.onWindowAttributesChanged(wl);
        dialog.setCancelable(true);//点击外围解散
        dialog.show();

        //取消拍照
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //拍照
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用系统相机
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageFile = new File(Environment.getExternalStorageDirectory(),getTime()+".jpg");
                imageUri = Uri.fromFile(imageFile);
                i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                dialog.dismiss();
                startActivityForResult(i,1);
            }
        });


        //从相册选择
        btn_choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                dialog.dismiss();
                startActivityForResult(i,2);
            }
        });



    }

    public String getTime()
    {
        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate=new Date();
        String str=format.format(curDate);
        return str;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            movePicture(imageUri.getPath());
        }
        else if (requestCode == 2)
        {
            if (data != null)
            {
                //获取图片绝对路径
                ContentResolver resolver = getActivity().getContentResolver();

                Uri originalUri = data.getData();        //获得图片的uri

               // 这里开始的第二部分，获取图片的路径：
                String[] proj = {MediaStore.Images.Media.DATA};

                //好像是android多媒体数据库的封装接口，具体的看Android文档
                Cursor cursor = getActivity().managedQuery(originalUri, proj, null, null, null);
                //按我个人理解 这个是获得用户选择的图片的索引值
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                //将光标移至开头 ，这个很重要，不小心很容易引起越界
                cursor.moveToFirst();
                //最后根据索引值获取图片路径
                String path = cursor.getString(column_index);
                movePicture(path);
            }else
            {
                Toast.makeText(getActivity(),"您没有选中照片",Toast.LENGTH_SHORT).show();
            }
        }
    }


    //仿QQ空间说说照片
    public void movePicture(String path)
    {
        bitmap_uri_submit.add(path);
        Log.d("sky",path);
            if (path != null)
            {
                bitmap_submit = BitmapFactory.decodeFile(path);
                Bitmap bitmap_small = ThumbnailUtils.extractThumbnail(bitmap_submit, 300, 300);
                if (iv_first.getDrawable() == null)
                {
                    Log.d("sky","第一个为空");
                    iv_first.setImageBitmap(bitmap_small);
                }else if ( iv_second.getDrawable() == null)
                {
                    iv_second.setImageBitmap(bitmap_small);
                }else if ( iv_third.getDrawable() == null)
                {
                    iv_camera.setVisibility(View.INVISIBLE);
                    iv_third.setImageBitmap(bitmap_small);
                    Toast.makeText(getActivity(),"抱歉，您最多只能上传3张照片",Toast.LENGTH_SHORT).show();
                }
            }
    }

}
