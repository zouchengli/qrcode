<h1 id="content">Scan the qrcode implement login</h1>

<img src="/get-qrcode?token=${token}" alt="qrcode">
<script src="js/jquery-latest.js"></script>
<script>
    function checkToken() {
        $.ajax({
            url: "check-token?token=${token}",
            type: "GET",
            dataType: "text",
            async: false,
            success: function (status) {
                if (status === "scanning") $("#content").text("Please you in mobile phone confirm login!");
                else if (status === "scanned") window.location.href = '/success';
            }
        })
    }

    setInterval(checkToken, 1000);
</script>
