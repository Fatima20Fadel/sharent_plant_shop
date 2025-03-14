(function ($) {
    "use strict";

    // Spinner
    var spinner = function () {
        setTimeout(function () {
            if ($('#spinner').length > 0) {
                $('#spinner').removeClass('show');
            }
        }, 1);
    };
    spinner();

    // Initiate the wowjs
    new WOW().init();

    // Sticky Navbar
    $(window).scroll(function () {
        if ($(this).scrollTop() > 300) {
            $('.sticky-top').addClass('shadow-sm').css('top', '0px');
        } else {
            $('.sticky-top').removeClass('shadow-sm').css('top', '-100px');
        }
    });

    // Back to top button
    $(window).scroll(function () {
        if ($(this).scrollTop() > 300) {
            $('.back-to-top').fadeIn('slow');
        } else {
            $('.back-to-top').fadeOut('slow');
        }
    });
    $('.back-to-top').click(function () {
        $('html, body').animate({scrollTop: 0}, 1500, 'easeInOutExpo');
        return false;
    });

    // Facts counter
    $('[data-toggle="counter-up"]').counterUp({
        delay: 10,
        time: 2000
    });

    // Custom function to animate counters
    function animateCounter(id, target, duration) {
        let count = 0;
        let increment = target / (duration / 50);  // Duration in milliseconds

        let interval = setInterval(function() {
            count += increment;
            if (count >= target) {
                count = target;
                clearInterval(interval);
            }
            document.getElementById(id).textContent = Math.floor(count);
        }, 50);
    }

    // Start the counters on page load
    $(document).ready(function() {
        animateCounter('happyClientsCount', 1234, 2000); // Target is 1234, duration is 2000ms
        animateCounter('completedGardensCount', 567, 2000);
        animateCounter('dedicatedStaffCount', 98, 2000);
        animateCounter('awardsAchievedCount', 10, 2000);
    });

    // Portfolio isotope and filter
    var portfolioIsotope = $('.portfolio-container').isotope({
        itemSelector: '.portfolio-item',
        layoutMode: 'fitRows'
    });
    $('#portfolio-flters li').on('click', function () {
        $("#portfolio-flters li").removeClass('active');
        $(this).addClass('active');
        portfolioIsotope.isotope({filter: $(this).data('filter')});
    });

    // Testimonials carousel
    $(".testimonial-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 1000,
        items: 1,
        dots: false,
        loop: true,
        nav: true,
        navText: [
            '<i class="bi bi-chevron-left"></i>',
            '<i class="bi bi-chevron-right"></i>'
        ]
    });

    // Function to toggle favorite (added to window for global scope)
    window.toggleFavorite = function (button) {
        const plantId = button.getAttribute('data-plant-id');
        const wishlistId = button.getAttribute('data-wishlist-id');  // Get wishlistId from button attribute

        // Make the POST request to add the plant to the wishlist
        fetch(`/api/wishlist/${wishlistId}/add?plantId=${plantId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                plantId: plantId,
                wishlistId: wishlistId
            })
        })
        .then(response => response.json())
        .then(data => {
            console.log('Plant added to wishlist:', data);
        })
        .catch(error => {
            console.error('Error adding plant to wishlist:', error);
        });
    };

    // Function to remove plant from wishlist
    window.removeFromWishlist = function (button) {
        const plantId = button.getAttribute('data-plant-id');
        alert('Removed plant with ID: ' + plantId);
        button.closest('.col-lg-4').remove();
    };

    // Delete plant functionality using AJAX DELETE request
  $(document).on('click', '.delete-link', function (e) {
      e.preventDefault();

      var plantId = $(this).data('plant-id');
      console.log("Plant ID: " + plantId);  // Log the plantId to check if it's correctly retrieved

      if (plantId && confirm("Are you sure you want to delete this plant?")) {
          $.ajax({
              url: `/api/plants/delete/${plantId}`,  // Correct URL structure
              type: 'DELETE',  // Ensure the method is DELETE
              success: function(response) {
                  alert("Plant deleted successfully!");
                  $('#plant-' + plantId).remove();  // Remove the plant from the DOM
              },
              error: function(xhr, status, error) {
                  alert("Error deleting plant: " + error);
              }
          });
      }
  });


(jQuery);
