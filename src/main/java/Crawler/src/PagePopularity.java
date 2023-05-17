package Crawler.src;

import java.util.HashSet;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class PagePopularity {
    private List<PRRObject> prrList;

    public static void calculate(DB db){
        // get all the links from the database
        // using page rank algorithm made by google
        // construct a matrix of links n*n, where n is the number of links in the database
        // we put all the links on rows and columns, and we put 1/(number of links the link has) in the cell
        // then we multiply the matrix by matrix r that contains 1/n in all cells, it is n*1 matrix
        // till convergence
        // repeat until convergence
        List<PRRObject> prrList = db.getPRRObjects();
        Integer n = prrList.size();

        //construct the r matrix
        double[][] r = constructMatrixR(n);
        //construct the M matrix
        double[][] m = constructMatrixM(n, prrList);

//        System.out.println("M matrix");
//        printMatrixM(m);
//        System.out.println("==================================");
//        System.out.println("R matrix");
//        //print dimensions
//        printMatrixR(r);

        RealMatrix rMatrix = new Array2DRowRealMatrix(r);
        RealMatrix mMatrix = new Array2DRowRealMatrix(m);

        //calculate the page rank
        RealMatrix pageRank = rMatrix;
        for(int i = 0; i < 4; i++){
            pageRank = mMatrix.multiply(pageRank);
        }
        String url;
        double rank;
        //update the database
        for(int i=0;i<prrList.size();++i){
            url = prrList.get(i).getUrl();
            rank = pageRank.getRow(i)[0];
            db.insertPageRank(url,rank);
        }

    }

    private static void printMatrixM(double[][] m) {
        for(int i = 0; i < m.length; i++){
            for(int j = 0; j < m.length; j++){
                System.out.print(m[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void printMatrixR(double[][] R) {
        for(int i = 0; i < R.length; i++){
            System.out.print(R[i][0] + " ");
            System.out.println();
        }
    }

    private static double[][] constructMatrixM(Integer n, List<PRRObject> prrList){
        //construct the M matrix
        double[][] m = new double[n][n];
        for(int i = 0; i < n; i++){
            PRRObject prrObject = prrList.get(i);
            HashSet<String> links = prrObject.getLinks();
            for(int j = 0; j < n; j++){
                if(links.contains(prrList.get(j).getUrl()) && links.size() != 0)
                    m[i][j] = (double )1/links.size();
                else
                    m[i][j] = 0;
            }
        }
        return m;
    }

    private static double[][] constructMatrixR(Integer n){
        double[][] r = new double[n][1];
        for(int i = 0; i < n; i++){
            r[i][0] = (double) 1 /n;
        }
        return r;
    }

}
