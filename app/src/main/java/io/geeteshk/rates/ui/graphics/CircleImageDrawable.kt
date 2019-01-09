/*
 * Copyright 2019 Geetesh Kalakoti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.geeteshk.rates.ui.graphics

import android.graphics.*
import android.graphics.drawable.Drawable

class CircleImageDrawable(
    private val bitmap: Bitmap,
    private val borderColor: Int,
    private val borderWidth: Int) : Drawable() {

    private val bitmapHeight = bitmap.height
    private val bitmapWidth = bitmap.width

    private lateinit var bitmapShader: BitmapShader

    private val bounds = RectF()
    private val drawableRect = RectF()
    private val borderRect = RectF()

    private val shaderMatrix = Matrix()
    private val bitmapPaint = Paint()
    private val borderPaint = Paint()

    private var drawableRadius = 0F
    private var borderRadius = 0F

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(bounds.width() / 2, bounds.height() / 2, drawableRadius, bitmapPaint)
        if (borderWidth != 0) {
            canvas.drawCircle(bounds.width() / 2, bounds.height() / 2, borderRadius, borderPaint)
        }
    }

    private fun setup() {
        bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        bitmapPaint.isAntiAlias = true
        bitmapPaint.shader = bitmapShader

        borderPaint.style = Paint.Style.STROKE
        borderPaint.isAntiAlias = true
        borderPaint.color = borderColor
        borderPaint.strokeWidth = borderWidth.toFloat()

        borderRect.set(bounds)
        borderRadius = Math.min((borderRect.height() - borderWidth) / 2, (borderRect.width() - borderWidth) / 2)

        drawableRect.set(borderRect)
        drawableRect.inset(borderWidth.toFloat(), borderWidth.toFloat())
        drawableRadius = Math.min(drawableRect.height() / 2, drawableRect.width() / 2)

        updateShaderMatrix()
        invalidateSelf()
    }

    private fun updateShaderMatrix() {
        var scale = 0F
        var dx = 0F
        var dy = 0F

        shaderMatrix.set(null)

        if (bitmapWidth * drawableRect.height() > drawableRect.width() * bitmapHeight) {
            scale = drawableRect.height() / bitmapHeight
            dx = (drawableRect.width() - bitmapWidth * scale) * 0.5F
        } else {
            scale = drawableRect.width() / bitmapWidth
            dy = (drawableRect.height() - bitmapHeight * scale) * 0.5F
        }

        shaderMatrix.setScale(scale, scale)
        shaderMatrix.postTranslate(dx + 0.5F + drawableRect.left, dy + 0.5F + drawableRect.top)

        bitmapShader.setLocalMatrix(shaderMatrix)
    }

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        this.bounds.set(bounds)
        setup()
    }

    override fun setAlpha(p0: Int) {
        bitmapPaint.alpha = p0
        invalidateSelf()
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

    override fun setColorFilter(p0: ColorFilter?) {
        bitmapPaint.colorFilter = p0
        invalidateSelf()
    }
}