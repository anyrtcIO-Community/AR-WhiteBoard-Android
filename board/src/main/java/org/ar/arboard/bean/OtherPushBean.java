package org.ar.arboard.bean;

import java.util.List;

/**
 * Created by liuxiaozhong on 2017/12/26.
 * other push paint data bean
 */

public class OtherPushBean {


    private int code;
    private long current_time;
    private List<ResultBean> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(long current_time) {
        this.current_time = current_time;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * board_revoke_time : 0
         * board_number : 1
         * board_seqid : 20171221145907633549
         * board_anyrtcid : 4008106666
         * boardid : 12402
         * board_state : 0
         * board_localid : android:4008106666:BpdCdkqShibTL1Qc9ad3RdcDUxi58t21-144952
         * board_appid : RTMPC_Line
         * board_data : {"DCanvasHeight":1025,"DCanvasWidth":1439,"DColor":"#FF0000","DEndX":977.3159,"DEndY":511.47546,"DPoint":"[[975.8639,504.7644],[971.9031,483.89435],[965.442,453.5385],[956.9221,421.826],[951.8345,407.58646],[940.7042,382.80685],[929.52563,363.38983],[916.9501,348.2293],[901.6599,334.98688],[888.3623,324.64847]]","DStartX":977.3159,"DStartY":511.47546,"DType":0,"DWidth":10}
         * board_time : 1514253797857
         */

        private long board_revoke_time;
        private int board_number;
        private String board_seqid;
        private String board_anyrtcid;
        private int boardid;
        private int board_state;
        private String board_localid;
        private String board_appid;
        private String board_data;
        private long board_time;

        public long getBoard_revoke_time() {
            return board_revoke_time;
        }

        public void setBoard_revoke_time(long board_revoke_time) {
            this.board_revoke_time = board_revoke_time;
        }

        public int getBoard_number() {
            return board_number;
        }

        public void setBoard_number(int board_number) {
            this.board_number = board_number;
        }

        public String getBoard_seqid() {
            return board_seqid;
        }

        public void setBoard_seqid(String board_seqid) {
            this.board_seqid = board_seqid;
        }

        public String getBoard_anyrtcid() {
            return board_anyrtcid;
        }

        public void setBoard_anyrtcid(String board_anyrtcid) {
            this.board_anyrtcid = board_anyrtcid;
        }

        public int getBoardid() {
            return boardid;
        }

        public void setBoardid(int boardid) {
            this.boardid = boardid;
        }

        public int getBoard_state() {
            return board_state;
        }

        public void setBoard_state(int board_state) {
            this.board_state = board_state;
        }

        public String getBoard_localid() {
            return board_localid;
        }

        public void setBoard_localid(String board_localid) {
            this.board_localid = board_localid;
        }

        public String getBoard_appid() {
            return board_appid;
        }

        public void setBoard_appid(String board_appid) {
            this.board_appid = board_appid;
        }

        public String getBoard_data() {
            return board_data;
        }

        public void setBoard_data(String board_data) {
            this.board_data = board_data;
        }

        public long getBoard_time() {
            return board_time;
        }

        public void setBoard_time(long board_time) {
            this.board_time = board_time;
        }
    }
}
