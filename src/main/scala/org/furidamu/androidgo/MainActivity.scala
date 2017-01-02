package org.furidamu.androidgo

import android.app.Activity
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.support.v8.renderscript.Allocation
import android.support.v8.renderscript.Element
import android.support.v8.renderscript.Type
import android.support.v8.renderscript.RenderScript
import android.support.v7.app.AppCompatActivity
import android.view.View

class MainActivity extends AppCompatActivity {
    // allows accessing `.value` on TR.resource.constants
    implicit val context = this

      override def onCreate(savedInstanceState: Bundle): Unit = {
        super.onCreate(savedInstanceState)
        // type ascription is required due to SCL-10491
        val vh: TypedViewHolder.main = TypedViewHolder.setContentView(this, TR.layout.main)
        vh.text.setText(s"Hello world, from ${TR.string.app_name.value}")
      }

      def runBenchmark(view: View) {
        val vh: TypedViewHolder.main = TypedViewHolder.setContentView(this, TR.layout.main)

        val renderScript = RenderScript.create(this)
        val script = new ScriptC_network(renderScript)

        // Matrix A is n * m and B is m * p, then C = A * B is n * p.
        val n = 10
        // For now, we just use square matrices.
        val m = n
        val p = n

        val floatElement = Element.F32(renderScript)

        val aType = Type.createXY(renderScript, floatElement,  n, m)
        val bType = Type.createXY(renderScript, floatElement,  m, p)
        val cType = Type.createXY(renderScript, floatElement,  n, p)

        val aAlloc = Allocation.createTyped(renderScript, aType)
        val bAlloc = Allocation.createTyped(renderScript, bType)
        val cAlloc = Allocation.createTyped(renderScript, cType)
        val dummyAlloc = Allocation.createTyped(renderScript, cType, Allocation.USAGE_SCRIPT)

        val numOpsAlloc = Allocation.createTyped(renderScript, cType, Allocation.USAGE_SCRIPT)

        aAlloc.copyFrom((1 to n * m).map(_.toFloat).toArray)
        bAlloc.copyFrom((1 to m * p).map(_.toFloat).toArray)

        // script.set_matrixA(aAlloc);
        // script.set_matrixB(bAlloc);
        // script.set_m(m);
        // script.set_numOps(numOpsAlloc);

        val matrixC = new Array[Float](n * p)
        val matrixNumOps = new Array[Float](n * p)

        var text = ""
        for (i <- 1 to 10) {
          val start = System.nanoTime()

          script.forEach_mmul(dummyAlloc, numOpsAlloc)
          // cAlloc.copyTo(matrixC)
          numOpsAlloc.copy2DRangeTo(0, 0, n, p, matrixNumOps)

          val elapsedSeconds = (System.nanoTime() - start) / 1e9

          val actualOps = matrixNumOps.sum
          val numOps = m.toDouble * n.toDouble * p.toDouble

          text += s"Performed ${actualOps} / ${numOps} ops in ${elapsedSeconds} s;"
          text += s"${formatFlops(numOps, elapsedSeconds)}\n\n"
          vh.text.setText(text)
        }
    }

    def formatFlops(numOps: Double, elapsedSeconds: Double): String = {
      val flops = numOps / elapsedSeconds
      val prefixes = List("", "k", "M", "G", "T", "P", "E", "Z", "Y")
      val order = Math.log10(flops).toInt / 3
      s"${flops / Math.pow(10, order * 3)} ${prefixes(order)}FLOPS"
    }
}
