import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.*;
import java.io.*;

public class Time {
	static String target = "https://sugang.hongik.ac.kr/cn1000.jsp";	// 원하는 url입력
	static int aa = 0;
	static int bb = 0;
	static int cc = 0;
	static long t1 = 0;
	static int check1 = 0;
	static int check2 = 0;
	public static void main(String[] args) {
		try {
			while(bb != 1) {	// 가장 처음으로 초단위가 바뀔때 bb = 1로 초기화된다. 이 때가 구할 수 있는 가장 정확한 x.00초 이다.
								// 나는 여기에 ping값을 추가하여 더 정확한 시계를 만들었다.
								// 단순하게 서버에 패킷을 보낼 때 x만큼, 받을 때 x만큼 시간이 걸린다고 한다면, 가장 처음으로 초단위가 바뀔 때 ( bb=1 이 될 때 ) 오차범위는 +-ping 만큼이다.
								// 그 이유는, 만약 1초에서 2초로 바뀌었다고 하고, x가 0.01초라고 한다면, 서버의 입장에서 생각했을 때 내가 서버로 패킷을 1.98초에 보내서
								// 1.99초에 서버에 도달(사실은 더 2초에 가까운 1.99999...초)했다고 한다면, 서버는 나에게 1초라고 응답할 것이고, 나는 다시 x만큼의 시간이 걸려서 받는다.
								// 그리고나서 아직 초가 바뀌지 않았으므로, 바로 다시 패킷을 보내는데, 이 때 내가 서버에 패킷을 보낸 시간은 정확히 2초일 때 패킷을 보내기 시작한다.
								// 따라서, 이 패킷을 다시 돌려받을 때까지는 2*x = 0.02초가 걸리게 되고, 나는 실제 서버시간이 2.02초일때 2초라는 대답을 받게된다.
								// 위가 가장 최악의 경우이며, 가장 최선의 경우에는 내가 보낸 패킷이 정확히 2초가 될때 서버에서 받는 경우이고, 이 때에는 x = 0.01초가 걸려서 2초라는 대답을 받게된다.
								// 즉, (1/2 pings ~ 3/2 pings) 의 오차가 있으며, 평균적으로 pings만큼의  오차가 발생함을 알 수 있다.
								// 따라서, 나중에 pings값을 빼줌으로써 더 정확한 서버시간을 알 수 있게 했다.
				Date();
			}
		} catch(Exception e) { }
		
		return;
	}
	public static void Show(int h, int m, int s, long pings) {	// Date()함수를 통해 받은 서버시간을 1초 간격으로 표시하는 함수
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));	// 화면 출력 시간이 오차에 영향을 줄 수 있기 때문에 가장 빠른 I/O 사용
			long s2 = s;
			long sum = 0;
			while(true) {
				long t = System.currentTimeMillis();	// 내 컴퓨터의 현재시간을 MILLISECONDS 단위로 구한다.
				if(check1 == 0) {	// 아직 이 함수에 한번도 들어오지 않은 상태이다. 이 때에는 t1 값이 아직 0이므로, 최초에 함수에 들어온 경우에는 t1을 초기화하기 위해서 구분했다.
					check1 = 1;
					t1 = t;
				}
				else {	// t1(직전시간)과 t(현재시간)의 차이만큼 ms의 시간이 흐른 것이므로, dif 라는 변수로 계속 차이를 구하며, sum으로 dif의 합을 계속해서 구한다.
					if(t > t1) {
						long dif = t - t1;
						sum += dif;
						
						if(check2 == 0 && sum >= 1000 - pings) {	// 우리는 서버로부터 시간을 받을 때 평균적으로 pings만큼의 오차를 가지고 있으므로,
																	// 최초에는 (1000 - pings)ms 만큼의 시간이 흘러야 1초가 증가하게 되는 것이다.
							check2 = 1;
							s2 += 1;
							if(s2 >= 60) {
								s2 -= 60;
								m += 1;
								if(m >= 60) {
									m = 0;
									h += 1;
									if(h >= 24) {
										h = 0;
									}
								}
							}
							sum = 0;
						}
						else {	// 이제부터는 1000ms마다 1초씩 증가하므로, sum이 1000이상이 되면 1초가 증가했음을 알 수 있고, 그렇다면 화면에 시간을 출력한다.
							if(sum >= 1000) {
								s2 += 1;
								if(s2 >= 60) {
									s2 -= 60;
									m += 1;
									if(m >= 60) {
										m = 0;
										h += 1;
										if(h >= 24) {
											h = 0;
										}
									}
								}
								bw.write(h + ":" + m + ":" + s2 + "\n");
								bw.flush();
								sum -= 1000;
							}
						}
					}
				}
				t1 = t;
			}
		} catch(Exception e) {}
	}
	public static void Date()
	{
		try {
			URL url = new URL(target);
			Date start = new Date();
			URLConnection conn = url.openConnection();
        
			Map<String, List<String>> headers = conn.getHeaderFields();
			String str_date = headers.get("Date").get(0);
			int h1 = Character.getNumericValue(str_date.charAt(17));
			int h2 = Character.getNumericValue(str_date.charAt(18));
			int h3 = h1*10 + h2;
			h3 += 9;
			if(h3 > 24) {
				h3 -= 24;
			}
			
			int m1 = Character.getNumericValue(str_date.charAt(20));
			int m2 = Character.getNumericValue(str_date.charAt(21));
			int m3 = m1*10 + m2;
			
			int s1 = Character.getNumericValue(str_date.charAt(23));
			int s2 = Character.getNumericValue(str_date.charAt(24));
			int s3 = s1*10 + s2;
			if(aa != s3 && cc == 1) { // 처음으로 초가 바뀐 상황 딱 정시일 가능성이 높다.
				bb = 1;
		        int timeToRespond = 0;
		        Date stop = new Date();
		        timeToRespond = (int)(stop.getTime() - start.getTime());
		        Show(h3,m3,s3,timeToRespond);
			}
			else {
				aa = s3;
			}
			
			cc = 1;
			
		} catch (Exception e) {	}
		
		
		return;
	}
}
