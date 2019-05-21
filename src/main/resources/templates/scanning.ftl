<script src="js/jquery-latest.js"></script>
<script>
    function confirm() {
        $.ajax({
            url: "confirm-login?token=${token}",
            type: "post",
            dataType: "json",
            async: false,
            success: function (reply) {
                if (reply === true) window.location.href = "/success"
            }
        })
    }
</script>

<button onclick="confirm()" style="width: 500px;height: 200px;font-size: 80px;text-align: center">Confirm Login</button>

