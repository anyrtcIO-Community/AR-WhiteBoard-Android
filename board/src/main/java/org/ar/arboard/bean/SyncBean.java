package org.ar.arboard.bean;

import java.util.List;

/**
 * Created by liuxiaozhong on 2017/11/6.
 */

public class SyncBean {

    private int code;
    private long current_time;
    private DocInfoBean doc_info;
    private List<BoardInfoBean> board_info;

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

    public DocInfoBean getDoc_info() {
        return doc_info;
    }

    public void setDoc_info(DocInfoBean doc_info) {
        this.doc_info = doc_info;
    }

    public List<BoardInfoBean> getBoard_info() {
        return board_info;
    }

    public void setBoard_info(List<BoardInfoBean> board_info) {
        this.board_info = board_info;
    }

    public static class DocInfoBean {
        /**
         * sys_docs_curt_number : 1
         * sys_docs_is_edit : 1
         */

        private int sys_docs_curt_number;
        private int sys_docs_is_edit;

        public int getSys_docs_curt_number() {
            return sys_docs_curt_number;
        }

        public void setSys_docs_curt_number(int sys_docs_curt_number) {
            this.sys_docs_curt_number = sys_docs_curt_number;
        }

        public int getSys_docs_is_edit() {
            return sys_docs_is_edit;
        }

        public void setSys_docs_is_edit(int sys_docs_is_edit) {
            this.sys_docs_is_edit = sys_docs_is_edit;
        }
    }

    public static class BoardInfoBean {
        /**
         * sys_board_appid : RTMPC_Line
         * sys_board_number : 1
         * sys_board_seqid : 20171221145907633549
         * sys_board_anyrtcid : 4008106666
         * sys_board_background : http://192.168.1.112:8899/documents/image/document_20171221144943917230_1.png
         * sys_board_time : 1513923193035
         * board_info_list : [{"boardid":12492,"board_localid":"iOS:VhkUoWiDM7FIJ2a1TDwhTETVdzJs4Dzb","board_number":1,"board_seqid":"20171221145907633549","board_anyrtcid":"4008106666","board_data":{"DWidth":2,"DColor":"#0000FF","DCanvasWidth":500,"DPoint":[[61,103],[75,103],[84.5,102],[98,100],[116,96.5],[139.5,92],[163.5,88],[187,85],[209,83],[231,82.5]],"DCanvasHeight":375,"DStartY":103,"DStartX":61,"DType":0},"board_state":"0","board_revoke_time":0,"board_time":1514271732066},{"boardid":12493,"board_localid":"iOS:6l2ftallADdPgvJhD8JFr484jEPbjbcp","board_number":1,"board_seqid":"20171221145907633549","board_anyrtcid":"4008106666","board_data":{"DWidth":2,"DColor":"#0000FF","DCanvasWidth":500,"DPoint":[[337.5,66.5],[345.5,71.5],[347.5,73],[349.5,74],[352,75.5],[355.5,77],[358.5,78.5],[361.5,80.5],[364.5,82.5],[367.5,84],[369.5,86],[370.5,86.5],[372,88],[373,88.5],[373.5,89],[374.5,90],[375.5,90.5]],"DCanvasHeight":375,"DStartY":66,"DStartX":337,"DType":0},"board_state":"0","board_revoke_time":0,"board_time":1514271746908},{"boardid":12501,"board_localid":"iOS:q3thUEd5qeYfRR6j5bKLCExFLuWDbZ8f","board_number":1,"board_seqid":"20171221145907633549","board_anyrtcid":"4008106666","board_data":{"DWidth":2,"DColor":"#0000FF","DCanvasWidth":500,"DPoint":[[166,268.5],[169.5,266.5],[176,261.5],[187,252.5],[196,245],[214.5,230.5],[234,215.5],[250,205],[260.5,199],[264,198],[265.5,197],[266,197]],"DCanvasHeight":375,"DStartY":268,"DStartX":166,"DType":0},"board_state":"0","board_revoke_time":0,"board_time":1514274317369},{"boardid":12502,"board_localid":"iOS:jNYCeRrd56f2dUKKmskmW1yPAemQ7BPu","board_number":1,"board_seqid":"20171221145907633549","board_anyrtcid":"4008106666","board_data":{"DWidth":2,"DColor":"#0000FF","DCanvasWidth":500,"DPoint":[[276.5,256],[279,251],[285,237.5],[291,225.5],[302.5,199.5],[315.5,177],[325,165],[333,156],[338,152]],"DCanvasHeight":375,"DStartY":256,"DStartX":276,"DType":0},"board_state":"0","board_revoke_time":0,"board_time":1514274317860},{"boardid":12503,"board_localid":"iOS:C7trpdNscri2lUdknosBWAGwhoymor7k","board_number":1,"board_seqid":"20171221145907633549","board_anyrtcid":"4008106666","board_data":{"DWidth":2,"DColor":"#0000FF","DCanvasWidth":500,"DPoint":[[368.5,252.5],[371,245.5],[372,233.5],[371.5,209],[372,190.5],[373.5,178]],"DCanvasHeight":375,"DStartY":252,"DStartX":368,"DType":0},"board_state":"0","board_revoke_time":0,"board_time":1514274318272},{"boardid":12504,"board_localid":"iOS:Wmi9iT9vBYQgPyKQQbojt5ZHUlCp2mty","board_number":1,"board_seqid":"20171221145907633549","board_anyrtcid":"4008106666","board_data":{"DWidth":2,"DColor":"#0000FF","DCanvasWidth":500,"DPoint":[[136.5,146.5],[157,144],[192.5,140.5],[256,138.5],[294.5,140],[357.5,150.5],[384.5,154],[390,154.5]],"DCanvasHeight":375,"DStartY":146,"DStartX":136,"DType":0},"board_state":"0","board_revoke_time":0,"board_time":1514274331324},{"boardid":12505,"board_localid":"iOS:UD3NrAaeP4t71WD7OZZ1382yKtiAqCep","board_number":1,"board_seqid":"20171221145907633549","board_anyrtcid":"4008106666","board_data":{"DWidth":2,"DColor":"#0000FF","DCanvasWidth":500,"DPoint":[[259.5,192.5],[256,203],[255.5,208],[253.5,222.5],[251,249],[249.5,268.5],[246,293.5],[244,303],[242.5,310.5],[239.5,316.5],[236,319.5],[231.5,318.5]],"DCanvasHeight":375,"DStartY":192,"DStartX":259,"DType":0},"board_state":"0","board_revoke_time":0,"board_time":1514274331849},{"boardid":12506,"board_localid":"iOS:cPxprAne67MngUQgcM4gkm3MH5A3w1T2","board_number":1,"board_seqid":"20171221145907633549","board_anyrtcid":"4008106666","board_data":{"DWidth":2,"DColor":"#0000FF","DCanvasWidth":500,"DPoint":[[126,260.5],[147.5,254],[184,242],[236,230.5],[309,224],[388,224.5],[437,235]],"DCanvasHeight":375,"DStartY":260,"DStartX":126,"DType":0},"board_state":"0","board_revoke_time":0,"board_time":1514274332199},{"boardid":12508,"board_localid":"android:4008106666:4l46G374g2G2eL9c98x8KSJ6443f2438-164352","board_number":1,"board_seqid":"20171221145907633549","board_anyrtcid":"4008106666","board_data":{"DCanvasHeight":1080,"DCanvasWidth":1439,"DColor":"#FF0000","DEndX":405.6637,"DEndY":552.48846,"DPoint":"[[406.22235,557.60156],[408.71075,562.05255],[413.31757,568.6839],[420.8598,578.21967],[430.41278,588.21735],[440.84204,599.6415],[450.28088,609.6228],[456.85095,617.642],[463.2799,624.0684],[467.17926,629.5123],[470.17987,633.513],[472.177,635.9591],[474.1756,637.9569],[475.62726,639.9518],[475.62726,641.94635],[475.62726,642.4052]]","DStartX":405.6637,"DStartY":552.48846,"DType":0,"DWidth":10},"board_state":"0","board_revoke_time":0,"board_time":1514274668737},{"boardid":12509,"board_localid":"iOS:TGRpWdtZwvVzL3X3xfivcS8rWMF58clH","board_number":1,"board_seqid":"20171221145907633549","board_anyrtcid":"4008106666","board_data":{"DWidth":2,"DColor":"#0000FF","DCanvasWidth":500,"DPoint":[[223,102.5],[223,105.5],[223,106.5],[223,108],[223.5,110],[224,112.5],[225.5,115.5],[229.5,121],[234.5,128.5],[242,139.5],[252,154.5],[262,171.5],[273,189.5],[285,212.5],[295,235]],"DCanvasHeight":375,"DStartY":102,"DStartX":223,"DType":0},"board_state":"0","board_revoke_time":0,"board_time":1514275323457},{"boardid":12510,"board_localid":"iOS:6NhCl8oGK10M9EoLqQb99BAa4VGBB5wa","board_number":1,"board_seqid":"20171221145907633549","board_anyrtcid":"4008106666","board_data":{"DWidth":2,"DColor":"#0000FF","DCanvasWidth":500,"DPoint":[[171,290],[183,262],[203,241.5],[234,218.5],[264.5,198.5],[302,180.5],[343,163.5]],"DCanvasHeight":375,"DStartY":290,"DStartX":171,"DType":0},"board_state":"0","board_revoke_time":0,"board_time":1514275503841},{"boardid":12511,"board_localid":"iOS:6ROO651M5ycg47L8AjdwA9Wrbz49qSPO","board_number":1,"board_seqid":"20171221145907633549","board_anyrtcid":"4008106666","board_data":{"DWidth":2,"DColor":"#0000FF","DCanvasWidth":500,"DPoint":[[368.5,225.5],[369,212.5],[369,199],[370,183],[370.5,163.5],[370.5,143.5],[370.5,123],[367.5,104.5],[364,87]],"DCanvasHeight":375,"DStartY":225,"DStartX":368,"DType":0},"board_state":"0","board_revoke_time":0,"board_time":1514275551496},{"boardid":12512,"board_localid":"iOS:K8AxK2Xwhf6o2zAVkPNJnP9rLy9ZPXiA","board_number":1,"board_seqid":"20171221145907633549","board_anyrtcid":"4008106666","board_data":{"DWidth":2,"DColor":"#0000FF","DCanvasWidth":500,"DPoint":[[429.5,113],[454.5,108.5],[469,102.5],[483.5,96.5],[493.5,93]],"DCanvasHeight":375,"DStartY":113,"DStartX":429,"DType":0},"board_state":"0","board_revoke_time":0,"board_time":1514275611621},{"boardid":12513,"board_localid":"iOS:1tPv4UJriLJusHVwWY7slJKdM1PxLBYl","board_number":1,"board_seqid":"20171221145907633549","board_anyrtcid":"4008106666","board_data":{"DWidth":2,"DColor":"#0000FF","DCanvasWidth":500,"DPoint":[[426.5,179],[448.5,160.5],[468.5,148.5],[485,139.5]],"DCanvasHeight":375,"DStartY":179,"DStartX":426,"DType":0},"board_state":"0","board_revoke_time":0,"board_time":1514275613373},{"boardid":12514,"board_localid":"iOS:fw5WiLxnuxomRhKAOrc4RRSXoSr0AyTs","board_number":1,"board_seqid":"20171221145907633549","board_anyrtcid":"4008106666","board_data":{"DWidth":2,"DColor":"#0000FF","DCanvasWidth":500,"DPoint":[[405,261],[424,230],[438,214.5],[453,200.5],[464,192.5],[475.5,186]],"DCanvasHeight":375,"DStartY":261,"DStartX":405,"DType":0},"board_state":"0","board_revoke_time":0,"board_time":1514275614108},{"boardid":12515,"board_localid":"iOS:CFvfxqKE2SVVW7zFuEs7FZglJQFrsxo4","board_number":1,"board_seqid":"20171221145907633549","board_anyrtcid":"4008106666","board_data":{"DWidth":2,"DColor":"#0000FF","DCanvasWidth":500,"DPoint":[[108.5,257.5],[113.5,247.5],[116.5,242.5],[119,236.5],[123,229],[128,218.5],[134.5,208],[140.5,197.5],[146.5,187.5],[151.5,179.5],[155,173],[159.5,168],[161,164.5],[162.5,163.5],[163,163.5]],"DCanvasHeight":375,"DStartY":257,"DStartX":108,"DType":0},"board_state":"0","board_revoke_time":0,"board_time":1514275646690}]
         */

        private String sys_board_appid;
        private int sys_board_number;
        private String sys_board_seqid;
        private String sys_board_anyrtcid;
        private String sys_board_background;
        private long sys_board_time;
        private String board_info_list;

        public String getSys_board_appid() {
            return sys_board_appid;
        }

        public void setSys_board_appid(String sys_board_appid) {
            this.sys_board_appid = sys_board_appid;
        }

        public int getSys_board_number() {
            return sys_board_number;
        }

        public void setSys_board_number(int sys_board_number) {
            this.sys_board_number = sys_board_number;
        }

        public String getSys_board_seqid() {
            return sys_board_seqid;
        }

        public void setSys_board_seqid(String sys_board_seqid) {
            this.sys_board_seqid = sys_board_seqid;
        }

        public String getSys_board_anyrtcid() {
            return sys_board_anyrtcid;
        }

        public void setSys_board_anyrtcid(String sys_board_anyrtcid) {
            this.sys_board_anyrtcid = sys_board_anyrtcid;
        }

        public String getSys_board_background() {
            return sys_board_background;
        }

        public void setSys_board_background(String sys_board_background) {
            this.sys_board_background = sys_board_background;
        }

        public long getSys_board_time() {
            return sys_board_time;
        }

        public void setSys_board_time(long sys_board_time) {
            this.sys_board_time = sys_board_time;
        }

        public String getBoard_info_list() {
            return board_info_list;
        }

        public void setBoard_info_list(String board_info_list) {
            this.board_info_list = board_info_list;
        }
    }
}
