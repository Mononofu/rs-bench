package org.furidamu.androidgo

import android.app.Activity
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.support.v8.renderscript.Allocation
import android.support.v8.renderscript.Element
import android.support.v8.renderscript.Type
import android.support.v8.renderscript.RenderScript
import android.support.v7.app.AppCompatActivity

class MainActivity extends AppCompatActivity {
    // allows accessing `.value` on TR.resource.constants
    implicit val context = this

      override def onCreate(savedInstanceState: Bundle): Unit = {
        super.onCreate(savedInstanceState)
        // type ascription is required due to SCL-10491
        val vh: TypedViewHolder.main = TypedViewHolder.setContentView(this, TR.layout.main)
        vh.text.setText(s"Hello world, from ${TR.string.app_name.value}")
        vh.image.getDrawable match {
          case a: Animatable => a.start()
          case _ => // not animatable
        }

        val renderScript = RenderScript.create(this)
        val script = new ScriptC_network(renderScript)

        val numElements = 100
        val floatElement = Element.I32(renderScript)
        val arrayType = Type.createX(renderScript, floatElement,  numElements)
        val inputAlloc = Allocation.createTyped(renderScript, arrayType)
        val outputAlloc = Allocation.createTyped(renderScript, arrayType)

        script.forEach_mapper(inputAlloc, outputAlloc)

        val output = new Array[Int](numElements)
        outputAlloc.copyTo(output)

        vh.text.setText(s"Hello world, sum = ${output.sum}")
    }
}
