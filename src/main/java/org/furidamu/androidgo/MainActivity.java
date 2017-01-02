package org.furidamu.androidgo;

import android.app.Activity;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.Type;
import android.support.v8.renderscript.RenderScript;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
      protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RenderScript renderScript = RenderScript.create(this);
        ScriptC_network script = new ScriptC_network(renderScript);

        int numElements = 100;
        Element floatElement = Element.I32(renderScript);
        Type arrayType = Type.createX(renderScript, floatElement,  numElements);
        Allocation inputAlloc = Allocation.createTyped(renderScript, arrayType);
        Allocation outputAlloc = Allocation.createTyped(renderScript, arrayType);

        script.forEach_mapper(inputAlloc, outputAlloc);

        int[] output = new int[numElements];
        outputAlloc.copyTo(output);

        // vh.text.setText(s"Hello world, sum = ${output.sum}")
    }
}
