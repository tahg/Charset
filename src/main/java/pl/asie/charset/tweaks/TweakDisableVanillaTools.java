/*
 * Copyright (c) 2015-2016 Adrian Siekierka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.asie.charset.tweaks;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import pl.asie.charset.lib.ModCharsetLib;

public class TweakDisableVanillaTools extends Tweak {
	public TweakDisableVanillaTools() {
		super("tweaks", "disableVanillaStyleTools", "Setting to 1 makes vanilla-type tools ineffective. Setting to 2 also tries to remove their recipes.", false, 0);
	}

	@Override
	public boolean canTogglePostLoad() {
		return false;
	}

	@Override
	public void enable() {
		Set<Item> itemSet = new HashSet<Item>();
		for (ResourceLocation l : Item.REGISTRY.getKeys()) {
			Item i = Item.REGISTRY.getObject(l);
			if (i instanceof ItemPickaxe || i instanceof ItemAxe || i instanceof ItemSpade || i instanceof ItemSword) {
				i.setMaxDamage(1);
				itemSet.add(i);
			}
		}
		if (getMode() >= 2) {
			Iterator<IRecipe> iterator = CraftingManager.getInstance().getRecipeList().iterator();
			while (iterator.hasNext()) {
				ItemStack output = iterator.next().getRecipeOutput();
				if (output != null && itemSet.contains(output.getItem())) {
					iterator.remove();
					itemSet.remove(output.getItem());
					ModCharsetLib.logger.info("Disabled " + Item.REGISTRY.getNameForObject(output.getItem()).toString() + " (removed recipe)");
				}
			}
		}
		for (Item i : itemSet) {
			ModCharsetLib.logger.info("Disabled " + Item.REGISTRY.getNameForObject(i).toString());
		}
	}
}
