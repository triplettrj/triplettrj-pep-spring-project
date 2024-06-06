
/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

 package com.example.controller;

 import com.example.entity.Account;
 import com.example.entity.Message;
 import com.example.repository.AccountRepository;
 import com.example.repository.MessageRepository;
 import com.example.service.AccountService;
 import com.example.service.MessageService;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.*;
 
 import java.util.List;
 
 @RestController
 public class SocialMediaController {
 
     @Autowired
     private AccountService accountService;
 
     @Autowired
     private MessageService messageService;
 
     @Autowired
     private MessageRepository messageRepository;
 
     @PostMapping("/register")
     public ResponseEntity<Account> registerAccount(@RequestBody Account account) {
         try {
             Account registeredAccount = accountService.registerAccount(account);
             return ResponseEntity.ok(registeredAccount);
         } catch (IllegalArgumentException e) {
             if (e.getMessage().equals("Username already exists")) {
                 return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
             } else {
                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
             }
         }
     }
 
     @PostMapping("/login")
     public ResponseEntity<Account> loginAccount(@RequestBody Account account) {
         try {
             Account loggedInAccount = accountService.loginAccount(account);
             return ResponseEntity.ok(loggedInAccount);
         } catch (IllegalArgumentException e) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
         }
     }
 
 
     @PostMapping("/messages")
     public ResponseEntity<Message> createMessage(@RequestBody Message message) {
         try {
             Message createdMessage = messageService.createMessage(message);
             return ResponseEntity.ok(createdMessage);
         } catch (IllegalArgumentException e) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
         }
     }
 
     @GetMapping("/messages")
     public ResponseEntity<List<Message>> getAllMessages() {
         List<Message> messages = messageService.getAllMessages();
         return ResponseEntity.ok(messages);
     }
 
     @GetMapping("/messages/{messageId}")
     public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
         Message message = messageService.getMessageById(messageId);
         if (message != null) {
             return ResponseEntity.ok(message);
         } else {
             return ResponseEntity.ok().build();
         }
     }
 
     @DeleteMapping("/messages/{messageId}")
     public ResponseEntity<Integer> deleteMessage(@PathVariable Integer messageId) {
         if (messageRepository.existsById(messageId)) {
             messageRepository.deleteById(messageId);
             return ResponseEntity.ok(1); 
         } else {
             return ResponseEntity.ok().build(); 
         }
     }

     @PatchMapping("/messages/{messageId}")
     public ResponseEntity<Integer> updateMessage(@PathVariable Integer messageId, @RequestBody Message message) {
         try {
             messageService.updateMessage(messageId, message);
             return ResponseEntity.ok(1); // Return 1 to indicate successful update
         } catch (IllegalArgumentException e) {
             return ResponseEntity.badRequest().body(null);
         }
     }
 
     @GetMapping("/accounts/{accountId}/messages")
     public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable Integer accountId) {
         List<Message> messages = messageService.getMessagesByAccountId(accountId);
         return ResponseEntity.ok(messages);
     }
 
     @ExceptionHandler(IllegalArgumentException.class)
     public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
     }
 
     @ExceptionHandler(RuntimeException.class)
     public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred bro!");
     }
 }