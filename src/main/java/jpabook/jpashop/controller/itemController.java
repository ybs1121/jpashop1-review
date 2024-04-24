package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class itemController {

    private final ItemService itemService;


    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(@ModelAttribute BookForm form, Model model) {
        Book book = new Book();
        book.setName(form.getName());
        book.setAuthor(form.getAuthor());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);

        return "redirect:/items";
    }

    @GetMapping("/items")
    public String getItemList(@ModelAttribute BookForm form, Model model) {
       model.addAttribute("items", itemService.findItems());
        return "items/itemList";
    }


    @GetMapping("/items/{id}/edit")
    public String getItemList(@PathVariable("id") Long id, Model model) {
        Book book = (Book)itemService.findOne(id);
        BookForm form = new BookForm();
        form.setId(book.getId());
        form.setName(book.getName());
        form.setAuthor(book.getAuthor());
        form.setPrice(book.getPrice());
        form.setStockQuantity(book.getStockQuantity());
        form.setIsbn(book.getIsbn());
        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{id}/edit")
    public String modItem(@ModelAttribute BookForm form, Model model) {
        Book book = new Book();
        book.setId(form.getId());
        book.setName(form.getName());
        book.setAuthor(form.getAuthor());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);

        List.of(Arrays.asList("123","456"));
        return "redirect:/items";
    }
}
