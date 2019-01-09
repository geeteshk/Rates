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

package io.geeteshk.rates.utils.extensions

import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.annotation.DrawableRes
import com.google.android.material.chip.Chip
import io.geeteshk.rates.ui.graphics.CircleImageDrawable

fun Chip.setRoundedIcon(@DrawableRes icon: Int) {
    val iconBitmap = BitmapFactory.decodeResource(resources, icon)

    /*val roundedDrawable = RoundedBitmapDrawableFactory.create(resources, iconBitmap)
    roundedDrawable.isCircular = true*/
    val roundedDrawable = CircleImageDrawable(iconBitmap, Color.argb(255, 255, 255, 255), 4)

    chipIcon = roundedDrawable
}