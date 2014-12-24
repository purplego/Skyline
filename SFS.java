package sky.queryalgorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

import sky.model.SkyTuple;
import sky.util.Constants;
import sky.util.Hpreprocess;
/**
 * @author Purple Wang
 * Jan 20, 2014
 */
public class SFS {
	
	/**
	 * sfsQueryByBNL方法，调用BNL算法计算已排序数据文件的skyline集合
	 * @param inFilename 给定的数据文件
	 * @return spList，求出的skyline集合，以list形式组织
	 */
	public static LinkedList<SkyTuple> sfsQueryByBNL(String inFilename){
		
		return BNL.bnlQuery(inFilename);
	}

	/**
	 * sfsQuery方法，利用SFS算法计算已排序数据文件的skyline集合
	 * @param inFilename 给定的数据文件
	 * @return spList，求出的skyline集合，以list形式组织
	 */
	public static LinkedList<SkyTuple> sfsQuery(String inFilename){
		
		String filePathIn = Constants.FILEDIRECTORY + inFilename;
		File fileIn=new File(filePathIn);
		Hpreprocess h_process = new Hpreprocess();
		
		LinkedList<SkyTuple> spList = new LinkedList<SkyTuple>();//保存已计算出的skyline点
		try{
			FileReader fReader=new FileReader(fileIn);
			BufferedReader bReader=new BufferedReader(fReader);
			
			String str_line = "";
			while(bReader.ready()){
				str_line = bReader.readLine();
				SkyTuple tuple = h_process.buildTupleFromStr(str_line);
				if(spList.isEmpty())
					spList.add(tuple);
				else{
					int i = 0;
					boolean flag = true;
					while(flag){
						int isDominate = 0;				//标志，用于判断支配关系是否成立
						//调用支配关系测试函数，测试两个对象之间的支配关系
						//SFS算法处理的是已排序文件，所有isDominate的值只可能是0或2，即后面的数据不会支配前面的数据
						isDominate = IsDominate.dominateBetweenTuples(spList.get(i), tuple);
						switch(isDominate){
						case 0:							//tuple被支配，直接删去该对象
							flag = false;				//跳出while循环，读下一个对象
							break;
						case 2:					//tuple与spList中的第i个对象互不支配，继续往后比较，进入下一次循环
							i++;				//比较对象变为链表中的下一个对象
							break;
						case 1:
							break;
						default:
							break;
						}
						if(i==spList.size()){	//如果比较到spList的最后一个对象而tuple仍然未被支配，则将其加入该链表的表尾
							spList.addLast(tuple);
							flag = false;		//结束while循环，读文件中的下一行
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return spList;
	}
}
