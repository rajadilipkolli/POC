DELETE FROM customer
WHERE  id IN ( 1, 2, 3 );

DELETE FROM address
WHERE  customer_id IN ( 1, 2, 3 );

DELETE FROM orders
WHERE  order_id = 1;

DELETE FROM post
WHERE  id IN ( 1, 2 );

DELETE FROM post_comment
WHERE  id IN ( 1, 2 ); 