package br.ufpe.cin.if1001.projeto_p3;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@Metadata(
        mv = {1, 1, 9},
        bv = {1, 0, 2},
        k = 1,
        d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007¨\u0006\u0005"},
        d2 = {"Lbr/ufpe/cin/if1001/projeto_p3/ExampleInstrumentedTest;", "", "()V", "useAppContext", "", "test sources for module app"}
)
public final class ExampleInstrumentedTest {
    @Test
    public final void useAppContext() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        Intrinsics.checkExpressionValueIsNotNull(appContext, "appContext");
        Assert.assertEquals("br.ufpe.cin.if1001.projeto_p3", appContext.getPackageName());
    }
}
