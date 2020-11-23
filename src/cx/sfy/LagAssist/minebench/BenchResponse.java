package cx.sfy.LagAssist.minebench;

public class BenchResponse {

	private int singlethread;
	private int multithread;

	public BenchResponse(int sth, int mth) {
		singlethread = sth;
		multithread = mth;
	}

	public int getMultithread() {
		return multithread;
	}

	public void setMultithread(int multithread) {
		this.multithread = multithread;
	}

	public int getSinglethread() {
		return singlethread;
	}

	public String getStringifiedSth() {
		if (singlethread < 0) {
			return "Unknown Score";
		}
		return String.valueOf(singlethread);
	}

	public String getStringifiedMth() {
		if (multithread < 0) {
			return "Unknown Score";
		}
		return String.valueOf(multithread);
	}

	public void setSinglethread(int singlethread) {
		this.singlethread = singlethread;
	}

}
