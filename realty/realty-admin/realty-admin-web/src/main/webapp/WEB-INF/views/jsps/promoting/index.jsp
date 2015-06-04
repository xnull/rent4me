<%--
  Created by IntelliJ IDEA.
  User: dionis
  Date: 04/06/15
  Time: 11:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div role="tabpanel">

    <!-- Nav tabs -->
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active">
            <a href="#vk_manual_post" aria-controls="profile" role="tab" data-toggle="tab">
                VK manual post
            </a>
        </li>
        <li role="presentation" ><a href="#vk_auth" aria-controls="vk_auth" role="tab" data-toggle="tab">
            VK Auth Setup
        </a>
        </li>

        <li role="presentation">
            <a href="#vkBulkSendForLast" aria-controls="messages" role="tab" data-toggle="tab">
                VK Bulk Send
            </a>
        </li>
        <li role="presentation"><a href="#settings" aria-controls="settings" role="tab" data-toggle="tab">Settings</a>
        </li>
    </ul>

    <!-- Tab panes -->
    <div class="tab-content"
         style="border-left: 1px solid lightgrey;border-right: 1px solid lightgrey;border-bottom: 1px solid lightgrey;">
        <div role="tabpanel" class="tab-pane active" id="vk_manual_post">
            <div>
                <form role="form" method="post" action="<c:url value="/secure/promoting/vk_post"/>">
                    <div class="form-group">
                        <label for="vkManualTargetGroup">Куда постить будем?</label>

                        <select id="vkManualTargetGroup" name="groupId" class="form-control">

                            <option value="-95509841" selected="selected"   >Страница</option>

                            <option value="-82219356"   >Группа</option>

                        </select>

                    </div>
                    <div class="form-group">
                        <label for="vkManualText">Text</label>
                        <textarea
                                rows="10"
                                class="form-control" name="text" id="vkManualText"
                                  placeholder="Message to post"></textarea>
                    </div>
                    <button type="submit" class="btn btn-default">Send Message</button>
                </form>
            </div>
        </div>
        <div role="tabpanel" class="tab-pane" id="vk_auth">
            <ol>
                <li>
                    <div>Get access code</div>
                    <div>
                        <button class="btn btn-primary" onclick="authWithVK()">Авторизация через ВК для получения code
                        </button>
                    </div>
                    <script type="text/javascript">
                        function authWithVK() {
                            document.location.href = "https://oauth.vk.com/authorize?client_id=4944878&redirect_uri=https://oauth.vk.com/blank.html&scope=73728&display=page&response_type=code";
                        }
                    </script>
                </li>
                <li>
                    <div>Exchange code to token</div>
                    <div>Current token: ${vkToken}</div>
                    <div>
                        <form role="form" method="post" action="<c:url value="/secure/promoting/vk_token"/>">
                            <div class="form-group">
                                <label for="code">Code</label>
                                <input type="text" class="form-control" name="code" id="code"
                                       placeholder="Code from previous step" value="">
                            </div>
                            <button type="submit" class="btn btn-default">Save</button>
                        </form>
                    </div>
                </li>
            </ol>
        </div>
        <div role="tabpanel" class="tab-pane" id="vkBulkSendForLast">
            <div>
                <form role="form" method="post" action="<c:url value="/secure/promoting/vk_bulk_post"/>">
                    <div class="form-group">
                        <label for="vkBulkHoursEnd">Не ранее чем</label>
                        <input type="text" class="form-control" name="hoursEnd" id="vkBulkHoursEnd"
                               placeholder="Ск. часов назад мин. опубликовано объявление?(Пример: 2)" value="1">
                    </div>
                    <div class="form-group">
                        <label for="vkBulkHoursStart">Не позднее чем</label>
                        <input type="text" class="form-control" name="hoursStart" id="vkBulkHoursStart"
                               placeholder="Ск. часов назад макс. опубликовано объявление?(Пример: 3)" value="3">
                    </div>
                    <button type="submit" class="btn btn-default">Опубликовать во ВК</button>
                </form>
            </div>
        </div>
        <div role="tabpanel" class="tab-pane" id="settings">...</div>
    </div>

</div>