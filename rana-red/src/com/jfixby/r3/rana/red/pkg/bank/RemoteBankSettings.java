
package com.jfixby.r3.rana.red.pkg.bank;

import com.jfixby.scarabei.api.collections.Collections;
import com.jfixby.scarabei.api.collections.Set;
import com.jfixby.scarabei.api.net.http.HttpURL;

public class RemoteBankSettings {
	public HttpURL bankURL;
	public final Set<String> tanks = Collections.newSet();

}
