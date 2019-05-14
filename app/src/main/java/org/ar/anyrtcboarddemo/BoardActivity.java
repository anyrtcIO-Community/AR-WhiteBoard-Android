package org.ar.anyrtcboarddemo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.ar.arboard.boardevent.ARBoardListener;
import org.ar.arboard.utils.ARBoardConfig;
import org.ar.arboard.weight.ARBoardView;

import java.util.ArrayList;
import java.util.List;

public class BoardActivity extends AppCompatActivity implements ARBoardListener {

    String roomId = "";
    boolean isHost;
    ARBoardView arBoardView;
    ImageButton ibtnTools;
    TextView tvPaint, tvColor, tvLineWidth, tv_pageNum;
    LinearLayout ll_tools, popColor, popWidth, popModel;
    ImageButton ib_paint, ib_line, ib_rect, ib_arrow, ibtn_next, ibtn_last;
    private int currentBrushModel = ARBoardConfig.BrushModel.Graffiti;//当前画笔类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        roomId = getIntent().getStringExtra("roomId");
        isHost = getIntent().getBooleanExtra("isHost", false);
        ibtnTools = findViewById(R.id.ibtn_tools);
        tvPaint = findViewById(R.id.tv_paint);
        tvColor = findViewById(R.id.tv_color);
        ibtn_last = findViewById(R.id.ibtn_last);
        ibtn_next = findViewById(R.id.ibtn_next);
        tvLineWidth = findViewById(R.id.tv_paint_width);
        arBoardView = findViewById(R.id.any_board);
        ll_tools = findViewById(R.id.ll_tools);
        popColor = findViewById(R.id.pop_color);
        popModel = findViewById(R.id.pop_model);
        popWidth = findViewById(R.id.pop_width);
        ib_paint = findViewById(R.id.ib_paint);
        ib_arrow = findViewById(R.id.ib_arrow);
        ib_rect = findViewById(R.id.ib_rect);
        ib_line = findViewById(R.id.ib_line);
        tv_pageNum = findViewById(R.id.tv_pageNum);

        //设置画板监听回调
        arBoardView.setWhiteBoardListener(this);
        //设置图片加载器
        arBoardView.setImageLoader(new BoardmageLoader());
        List<String> imageList = new ArrayList<>();
        imageList.add(DeveloperInfo.IMAGE1);
//        imageList.add("#FF0000");//纯色背景添加这个格式的颜色代码
        //初始化画板
        arBoardView.initWithRoomId(roomId, "888888888", isHost ? BoardApplication.hostId : BoardApplication.joinerId, imageList);
        //设置画笔类型
        arBoardView.getARBoardConfig().setBrushModel(isHost ? ARBoardConfig.BrushModel.TransformSync : ARBoardConfig.BrushModel.Transform);
    }

    @Override
    public void initBoardSuccess() {

    }

    @Override
    public void onBoardError(int nErrorCode) {

    }

    @Override
    public void onBoardServerDisconnect() {

    }

    @Override
    public void onBoardPageChange(final int currentPage, final int totlePage, String backgroundUrl) {
        BoardActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_pageNum.setText(currentPage + "/" + totlePage);
                if (totlePage <= 1) {
                    ibtn_last.setVisibility(View.GONE);
                    ibtn_next.setVisibility(View.GONE);
                } else {
                    if (currentPage == 1) {
                        ibtn_last.setVisibility(View.GONE);
                        ibtn_next.setVisibility(View.VISIBLE);
                    } else {
                        if (currentPage == totlePage) {
                            ibtn_last.setVisibility(View.VISIBLE);
                            ibtn_next.setVisibility(View.GONE);
                        } else {
                            ibtn_last.setVisibility(View.VISIBLE);
                            ibtn_next.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBoardDrawsChangeTimestamp(long timestamp) {

    }

    @Override
    public void onBoardMessage(String msgData) {

    }

    @Override
    public void onBoardDestroy() {
        BoardActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BoardActivity.this, "画板已退出", Toast.LENGTH_SHORT);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBoardLayout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isHost) {
            arBoardView.destoryBoard();
        }
        arBoardView.leave();
    }

    private void setBoardLayout() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) arBoardView.getLayoutParams(); //取控件mRlVideoViewLayout当前的布局参数
        final float height = this.getResources().getDisplayMetrics().heightPixels;
        final double width = height * 1.777777;//16:9
        // int height = (int) (0.56 * screenWidth);
        params.width = (int) width;// 强制设置控件的大小
        arBoardView.setLayoutParams(params); //使设置好的布局参数应用到控件
        arBoardView.requestLayout();
    }

    public void btnOnclick(View view) {
        switch (view.getId()) {
            case R.id.tv_clean:
                arBoardView.cleanCurrentDraw();
                break;
            case R.id.tv_undo:
                arBoardView.undo();
                break;
            case R.id.ibtn_next:
                arBoardView.nextPage(isHost);
                break;
            case R.id.ibtn_last:
                arBoardView.prePage(isHost);
                break;
            case R.id.tv_paint_width:
                updataToolsVisible(1);
                break;
            case R.id.tv_color:
                updataToolsVisible(0);
                break;
            case R.id.tv_paint:
                updataToolsVisible(2);
                break;
            case R.id.ibtn_tools:
                if (ll_tools.getVisibility() == View.VISIBLE) {
                    ll_tools.setVisibility(View.GONE);
                    ibtnTools.setSelected(false);
                    arBoardView.getARBoardConfig().setBrushModel(isHost ? ARBoardConfig.BrushModel.TransformSync : ARBoardConfig.BrushModel.Transform);
                } else {
                    ll_tools.setVisibility(View.VISIBLE);
                    ibtnTools.setSelected(true);
                    arBoardView.getARBoardConfig().setBrushModel(currentBrushModel);
                    updataPaintType();
                }
                break;
            case R.id.ib_width_a:
                arBoardView.getARBoardConfig().setBrushWidth(12f);
                popWidth.setVisibility(View.GONE);
                updataPaintType();
                break;
            case R.id.ib_width_b:
                arBoardView.getARBoardConfig().setBrushWidth(16f);
                popWidth.setVisibility(View.GONE);
                updataPaintType();
                break;
            case R.id.ib_width_c:
                arBoardView.getARBoardConfig().setBrushWidth(20f);
                popWidth.setVisibility(View.GONE);
                updataPaintType();
                break;
            case R.id.ib_paint:
                arBoardView.getARBoardConfig().setBrushModel(ARBoardConfig.BrushModel.Graffiti);
                currentBrushModel = ARBoardConfig.BrushModel.Graffiti;
                popModel.setVisibility(View.GONE);
                updataPaintType();
                break;
            case R.id.ib_line:
                arBoardView.getARBoardConfig().setBrushModel(ARBoardConfig.BrushModel.Line);
                currentBrushModel = ARBoardConfig.BrushModel.Line;
                popModel.setVisibility(View.GONE);
                updataPaintType();
                break;
            case R.id.ib_rect:
                arBoardView.getARBoardConfig().setBrushModel(ARBoardConfig.BrushModel.Rect);
                currentBrushModel = ARBoardConfig.BrushModel.Rect;
                popModel.setVisibility(View.GONE);
                updataPaintType();
                break;
            case R.id.ib_arrow:
                arBoardView.getARBoardConfig().setBrushModel(ARBoardConfig.BrushModel.Arrow);
                currentBrushModel = ARBoardConfig.BrushModel.Arrow;
                popModel.setVisibility(View.GONE);
                updataPaintType();
                break;
            case R.id.ib_red:
                arBoardView.getARBoardConfig().setBrushColor("#FF3A35");
                popColor.setVisibility(View.GONE);
                updataPaintType();
                break;
            case R.id.ib_green:
                arBoardView.getARBoardConfig().setBrushColor("#2CC233");
                popColor.setVisibility(View.GONE);
                updataPaintType();
                break;
            case R.id.ib_blue:
                arBoardView.getARBoardConfig().setBrushColor("#0077FF");
                popColor.setVisibility(View.GONE);
                updataPaintType();
                break;
            case R.id.ib_black:
                arBoardView.getARBoardConfig().setBrushColor("#000000");
                popColor.setVisibility(View.GONE);
                updataPaintType();
                break;
            case R.id.ibtn_exit:
                finish();
                break;

        }
    }

    private void updataPaintType() {
        Drawable drawable;
        switch (currentBrushModel) {
            case 1://涂鸦
                drawable = getResources().getDrawable(R.drawable.img_paint_select);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvPaint.setCompoundDrawables(null, drawable, null, null);
                break;
            case 2://箭头
                drawable = getResources().getDrawable(R.drawable.img_jiantou_select);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvPaint.setCompoundDrawables(null, drawable, null, null);
                break;
            case 3://直线
                drawable = getResources().getDrawable(R.drawable.img_line_select);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvPaint.setCompoundDrawables(null, drawable, null, null);
                break;
            case 4://矩形
                drawable = getResources().getDrawable(R.drawable.img_rect_select);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvPaint.setCompoundDrawables(null, drawable, null, null);
                break;
        }

        switch (arBoardView.getARBoardConfig().getBrushColor().toUpperCase()) {
            case "#FF3A35":
                drawable = getResources().getDrawable(R.drawable.img_red);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvColor.setCompoundDrawables(null, drawable, null, null);
                break;
            case "#0077FF":
                drawable = getResources().getDrawable(R.drawable.img_blue);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvColor.setCompoundDrawables(null, drawable, null, null);
                break;
            case "#000000":
                drawable = getResources().getDrawable(R.drawable.img_black);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvColor.setCompoundDrawables(null, drawable, null, null);
                break;
            case "#2CC233":
                drawable = getResources().getDrawable(R.drawable.img_green);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvColor.setCompoundDrawables(null, drawable, null, null);
                break;
            default:
                drawable = getResources().getDrawable(R.drawable.img_black);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvColor.setCompoundDrawables(null, drawable, null, null);
                break;
        }

        if (arBoardView.getARBoardConfig().getBrushWidth() > 1 && arBoardView.getARBoardConfig().getBrushWidth() <= 14) {
            drawable = getResources().getDrawable(R.drawable.img_paint_width_a);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLineWidth.setCompoundDrawables(null, drawable, null, null);
        } else if (arBoardView.getARBoardConfig().getBrushWidth() > 14 && arBoardView.getARBoardConfig().getBrushWidth() <= 18) {
            drawable = getResources().getDrawable(R.drawable.img_paint_width_b);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLineWidth.setCompoundDrawables(null, drawable, null, null);
        } else {
            drawable = getResources().getDrawable(R.drawable.img_paint_width_c);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvLineWidth.setCompoundDrawables(null, drawable, null, null);
        }
    }

    private void updataToolsVisible(int type) {
        if (type == 0) {
            popColor.setVisibility(View.VISIBLE);
            popWidth.setVisibility(View.GONE);
            popModel.setVisibility(View.GONE);
        } else if (type == 1) {
            popColor.setVisibility(View.GONE);
            popWidth.setVisibility(View.VISIBLE);
            popModel.setVisibility(View.GONE);
        } else {
            popColor.setVisibility(View.GONE);
            popWidth.setVisibility(View.GONE);
            popModel.setVisibility(View.VISIBLE);
            updataBrushModelType();
        }
    }

    private void updataBrushModelType() {
        switch (currentBrushModel) {
            case 1://涂鸦
                ib_line.setSelected(false);
                ib_paint.setSelected(true);
                ib_arrow.setSelected(false);
                ib_rect.setSelected(false);
                break;
            case 2://箭头
                ib_line.setSelected(false);
                ib_paint.setSelected(false);
                ib_arrow.setSelected(true);
                ib_rect.setSelected(false);
                break;
            case 3://直线
                ib_line.setSelected(true);
                ib_paint.setSelected(false);
                ib_arrow.setSelected(false);
                ib_rect.setSelected(false);
                break;
            case 4://矩形
                ib_line.setSelected(false);
                ib_paint.setSelected(false);
                ib_arrow.setSelected(false);
                ib_rect.setSelected(true);
                break;
        }

    }
}
