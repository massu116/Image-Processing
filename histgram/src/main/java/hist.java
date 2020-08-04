import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import java.io.*;

public class hist extends Application{
    //原画像の濃度値配列
    @Override
    public void start(Stage stage) throws Exception {
        Image img = new Image( "sample1.png" );		//画像ファイルロード
        int width = (int) img.getWidth();
        int height= (int) img.getHeight();

        //輝度値のみの配列を取得
        int aryLuminance[][] = getLuminanceArray(img, width, height);			//原画像の輝度値配列を取得

        //***** 原画像ヒストグラムの描画処理 *****
        int[] originalHist = getHistArray(aryLuminance, width, height);	//(1)仮のヒストグラム配列の取得。画像の濃度値配列を関数に渡し、ヒストグラム配列を受け取るようにする。

        try{
            File file = new File("/hoge/huga/piyo.csv");
            OutputStreamWriter osw  = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);

            for(int i=0;i<originalHist.length;i++){
                bw.write(String.valueOf(originalHist[i]));
                bw.newLine();
                System.out.println(String.valueOf(originalHist[i]));
            }
            bw.close();

        }catch (IOException e){
            System.out.println(e);
        }

    }

    /************************************************************************
     ************************************************************************
     **                                                                    **
     ** ヒストグラム用配列のためのメソッド  		                       **
     ** 濃度値配列を基に、1次元のヒストグラム配列を返す                    **
     **                                                                    **
     ************************************************************************
     ************************************************************************/
    public int[] getHistArray(int[][] aryLuminance, int width, int height){

        //ここにヒストグラム濃度値を要素番号、その濃度値の画素数を要素の値とする配列histを作成するように実装する。
        int hist[] = new int[256];

        //2重のforで1画素ずつ処理を行う。
        for(int y=0; y < height; y++ ){
            for(int x=0; x < width; x++ ){
                hist[aryLuminance[x][y]] += 1;
            }
        }

        return hist;
    }

    /************************************************************************
     *
     * カラー画像の各画素の濃度値から輝度値を求めるメソッド。
     * 戻り値は輝度値を格納した各画素の濃度値配列
     *
     ************************************************************************/
    public int[][] getLuminanceArray(Image img, int width, int height) {
        //カラー画像各画素の濃度値を読み取るため
        PixelReader reader = img.getPixelReader();

        //グレイスケール画像の濃度値値を格納する配列
        int luminancePixel[][] = new int[width][height];

        //各画素の濃度値のα値、R、G、Bを格納する変数
        int argb, alpha, red, green, blue;

        // 1画素ずつRGBの値から輝度値を求める。
        for(int y=0; y < height; y++){
            for(int x=0; x < width; x++){
                //論理積をとることで、シフト後の下位8bitをそのまま残し、上位24bitは0とする。
                argb = reader.getArgb( x, y);
                alpha = (argb >> 24) & 0xFF;
                red   = (argb >> 16) & 0xFF;
                green = (argb >>  8) & 0xFF;
                blue  =  argb        & 0xFF;
                //LuminancePixel[x][y] = (int)(0.299 * red + 0.587 * green + 0.114 * blue);
                luminancePixel[x][y] = green;
            }
        }

        return luminancePixel;
    }

}
