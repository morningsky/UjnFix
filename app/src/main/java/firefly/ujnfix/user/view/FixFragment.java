package firefly.ujnfix.user.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadBatchListener;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import firefly.ujnfix.R;
import firefly.ujnfix.user.model.FixEvent;
import firefly.ujnfix.user.model.User;

/**
 * Created by Phineas on 2015/7/27.
 */
public class FixFragment extends Fragment {
    private int flag_fixScene;
    private User curUser;//当前用户
    private Button btn_uploading;//提交报修
    private FixEvent curFixEvent;//当前报修事件
    private ProgressDialog pd_uploading;//图片上传对话框
    private EditText edt_location;//地点
    private Button btn_fixType;   //修理类型
    private EditText edt_details; //详情描述
    private Spinner sp_scene;     //选择维修场景
    private String location; //维修地点
    private String fixType;  //维修类型
    private String fixScene; //维修场景
    private String details;  //详情描述
    private String[] scenes = {"宿舍", "教室", "食堂", "图书馆", "教师公寓", "露天设备",};//Spinner的项目



//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what){
//                case 1:
//                    //上传图片
//                    imageUploading(PhotoFragment.bitmap_uri_submit);
//                    break;
//                default:
//                    break;
//            }
//        }
//    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        curUser = BmobUser.getCurrentUser(getActivity(), User.class);
        curFixEvent = new FixEvent();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_fix,container,false);
        initView(view);//初始化视图
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //加载拍照模块fragment
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.layout_photo,new PhotoFragment())
                .commit();


        //跳到维修类型选择界面
        btn_fixType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bringFlag();
                Intent i = new Intent(getActivity(),ChooseFixType.class);
                i.putExtra("flag_fixScene",flag_fixScene);
                startActivityForResult(i,1);
            }
        });

        btn_uploading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开启上传线程
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Message msg_img = new Message();
//                        msg_img.what = 1;
//                        handler.sendMessage(msg_img);//执行上传图片任务
//                    }
//                });


                initData();    //初始化数据

                if (notNull(curFixEvent))
                {
                    //将list装换为数组
                    String[] files = new String[PhotoFragment.bitmap_uri_submit.size()];
                    for (int i =0;i<PhotoFragment.bitmap_uri_submit.size();i++)
                    {
                        Log.d("where",i + "别这样：" + PhotoFragment.bitmap_uri_submit.get(i));
                        files[i] = PhotoFragment.bitmap_uri_submit.get(i);
                    }


                    if (files.length!=0)
                    {
                        //上传图片加数据更新
                        imageUploading(files);
                        sendMessage("有新的维修任务："+"地点： " +curFixEvent.getLocation() +"维修类型："
                                + curFixEvent.getFixType() +"请及时处理");
                    }else
                    {
                        sendMessage("有新的维修任务："+"地点： " +curFixEvent.getLocation() +"维修类型："
                                + curFixEvent.getFixType() +"请及时处理");
                        allUpdate();
                    }

                }else
                {
                    toast("抱歉，您有必填内容为空");
                }
            }
        });
        return view;
    }

    //给维修场景赋值 并决定传入维修类型选择页面的标记
    public void bringFlag()
    {
        switch (sp_scene.getSelectedItemPosition())
        {
            //"宿舍", "教室", "食堂", "图书馆", "教师公寓", "露天设备"
            case 0:
                flag_fixScene = 0;
                break;
            case 1:
                flag_fixScene = 1;
                break;
            case 2:
                flag_fixScene = 2;
                break;
            case 3:
                flag_fixScene = 3;
                break;
            case 4:
                flag_fixScene = 4;
                break;
            case 5:
                flag_fixScene = 5;
        }
    }


    //执行不为空的判断
    public boolean notNull(FixEvent f)
    {
        if (f.getDetails().equals("")){
            toast("抱歉，您没有填写任何报修详情内容，不能提交");
            return false;
        }
        if (f.getFixScene()==null)
        {
            toast("抱歉，您没有选择报修场景，不能提交");
            return false;
        }
        if (f.getFixType()==null)
        {
            toast("抱歉，您没有选择报修物品类型，不能提交");
            return false;
        }
        if (f.getLocation().equals(""))
        {
            toast("抱歉，您没有填写地点，不能提交");
            return false;
        }
        else
            return true;
    }


    //初始化视图
    public void initView(View v)
    {
        pd_uploading = new ProgressDialog(getActivity());

        //加载spinner的选项卡
        sp_scene = (Spinner) v.findViewById(R.id.sp_scene);
        ArrayAdapter<String> adapter  =new ArrayAdapter<String>(getActivity().getBaseContext(),R.layout.support_simple_spinner_dropdown_item,scenes);
        sp_scene.setAdapter(adapter);

        //默认选项为0
        sp_scene.setSelection(0);

        edt_details = (EditText) v.findViewById(R.id.edt_details);
        edt_location = (EditText) v.findViewById(R.id.edt_location);

        btn_fixType = (Button) v.findViewById(R.id.btn_fixType);
        btn_fixType.setClickable(false);  //默认不可点击
        btn_uploading = (Button) v.findViewById(R.id.btn_uploading);


    }

    //初始化数据
    public void initData()
    {

        curFixEvent.setSubmitUser(curUser);

        location = edt_location.getText().toString();
        curFixEvent.setLocation(location);

        details = edt_details.getText().toString();
        curFixEvent.setDetails(details);

        curFixEvent.setFixType(fixType);

        //设置标题 xx坏了
        curFixEvent.setTitle(fixType + "坏了");

        //默认状态为未解决
        curFixEvent.setSolvedState(1);

        switch (sp_scene.getSelectedItemPosition())
        {
            //"宿舍", "教室", "食堂", "图书馆", "教师公寓", "露天设备"
            case 0:
                fixScene = "宿舍";
                break;
            case 1:
                fixScene = "教室";
                break;
            case 2:
                fixScene = "食堂";
                break;
            case 3:
                fixScene = "图书馆";
                break;
            case 4:
                fixScene = "教师公寓";
                break;
            case 5:
                fixScene = "露天设备";
        }
        curFixEvent.setFixScene(fixScene);
    }


    //上传图片 + 数据更新到后台
    public void imageUploading(final String [] files)
    {
        if (files != null) {
            BmobProFile.getInstance(getActivity()).uploadBatch(files,new UploadBatchListener() {
                @Override
                public void onSuccess(boolean isFinish, String[] strings, String[] strings2, BmobFile[] bmobFiles) {
                    if (isFinish){
                        switch (bmobFiles.length)
                        {
                            case 3:
                                curFixEvent.setImageFile3(bmobFiles[2]);
                            case 2:
                                curFixEvent.setImageFile2(bmobFiles[1]);
                            case 1:
                                curFixEvent.setImageFile1(bmobFiles[0]);
                        }
                        toast("图片上传成功");
                        allUpdate();
                        //对话框消失
                        pd_uploading.dismiss();
                    }
                }
                @Override
                public void onProgress(int i, int i2, int i3, int i4) {
                    toast("当前正在上传第" + i +"个图片,进度"+ i2+ "总文件个数" + i3 + "总进度：" + i4);
                    pd_uploading.setTitle("维修信息正在提交，请稍候");
                    pd_uploading.setMessage("Loading...");
                    pd_uploading.setProgress(i4);
                    pd_uploading.show();
                }

                @Override
                public void onError(int i, String s) {
                    toast("图片上传失,请检查网络连接" + s);
                    pd_uploading.dismiss();
                }
            });
        }
    }

    //所有数据更新到数据库
    public void allUpdate()
    {
        curFixEvent.save(getActivity(),new SaveListener() {
            @Override
            public void onSuccess() {
                AlertDialog.Builder dia_ok = new AlertDialog.Builder(getActivity()) ;
                dia_ok.setCancelable(true)
                        .setTitle("UjnFix")
                        .setMessage("您的报修申请已提交，ujnfix工作人员将很快处理，请稍后")
                        .show();
                toast("报修成功" + curFixEvent.getSubmitUser().toString());
            }
            @Override
            public void onFailure(int i, String s) {
                toast("数据添加失败,请检查网络状况" + s);
            }
        });
    }

    //toast封装
    public void toast(String tag)
    {
        Toast.makeText(getActivity(),tag,Toast.LENGTH_SHORT).show();
    }

    //针对选择维修类型返回的结果
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode ==1)
        {
            //设置维修类型
            fixType = data.getStringExtra("select");
            btn_fixType.setText(fixType);
            Toast.makeText(getActivity(), "" + fixType,Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //发送推送给所有维修员
    public void sendMessage(String message)
    {
        BmobPushManager sendTask = new BmobPushManager(getActivity());
        BmobQuery queryFixer = new BmobQuery();
        queryFixer.addWhereEqualTo("fixer",true);
        sendTask.setQuery(queryFixer);
        JSONObject json = new JSONObject();
        try {
            json.put("alert",message);
            json.put("tag",2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendTask.pushMessage(json);
    }
}
