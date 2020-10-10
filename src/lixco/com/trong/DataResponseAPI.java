package lixco.com.trong;

public class DataResponseAPI {
	int err = 0;
	String msg = "";
	String dt = "";

	public int getErr() {
		return err;
	}

	public void setErr(int err) {
		this.err = err;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getDt() {
		return dt;
	}

	public void setDt(String dt) {
		this.dt = dt;
	}

	@Override
	public String toString() {
		return "{err:" + err + ", msg:" + msg + ", dt:" + dt + "}";
	}

}
