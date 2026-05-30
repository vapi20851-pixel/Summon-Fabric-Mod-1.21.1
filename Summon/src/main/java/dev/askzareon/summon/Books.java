package dev.askzareon.summon;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WrittenBookContent;

import java.util.List;

public final class Books {
	public static void drop(ServerPlayer p, int n) {
		switch (n) {
			case 1 -> give(p, "book.first.title", "book.first.page1");
			case 2 -> give(p, "book.second.title", "book.second.page1");
			case 3 -> give(p, "book.third.title", "book.third.page1");
			case 4 -> give(p, "book.fourth.title", "book.fourth.page1");
		}
	}

	private static void give(ServerPlayer p, String title, String page) {
		ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
		book.set(DataComponents.WRITTEN_BOOK_CONTENT, new WrittenBookContent(
				Filterable.passThrough(Component.translatable(title).getString()),
				"?",
				0,
				List.of(Filterable.passThrough(Component.translatable(page))),
				true
		));
		p.getInventory().add(book);
	}
}
